package com.baling.service.blockchain;

import com.baling.models.log.Log;
import com.baling.models.right.ERightType;
import com.baling.models.user.User;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.log.LogRepository;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.user.UserRepository;
import com.baling.sdk.FabricClient;
import com.baling.sdk.UserContext;
import com.baling.sdk.UserUtils;
import com.baling.util.CommonMethod;
import com.baling.util.SharedServiceUtil;
import com.sansec.MobileShieldEncInfo;
import org.bouncycastle.crypto.CryptoException;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ChainDataServiceImpl implements ChainDataService{
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    LogRepository logRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RightTypeRepository rightTypeRepository;

    @Autowired
    SharedServiceUtil sharedServiceUtil;

    @Override
    public DataResponse getTxnById(String id) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_QUERY))return CommonMethod.getReturnMessageError("无查询权限！");
        String[] initArgs={id};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_QUERY),"查询交易，编号："+id);
        logRepository.save(log);
        try {
            String JSONString= search("getOrder",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return CommonMethod.getReturnData(new JSONParser().parse(JSONString));
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return CommonMethod.getReturnMessageError(e.getMessage());
        }
    }

    @Override
    public DataResponse getTxnHistoryById(String id) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_QUERY_HISTORY))return CommonMethod.getReturnMessageError("无查询历史权限！");
        String[] initArgs={id};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_QUERY_HISTORY),"查询交易历史，编号："+id);
        logRepository.save(log);
        try {
            String JSONString= "["+search("getOrderHistory",initArgs)+"]";//适应统一搜索接口，放到数组里
            log.setOperateState(0);
            logRepository.save(log);
            return CommonMethod.getReturnData(new JSONParser().parse(JSONString));
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return CommonMethod.getReturnMessageError(e.getMessage());
        }
    }

    @Override
    public DataResponse searchTxs(DataRequest dataRequest) {
        Date startDateTime=dataRequest.getDate("startDateTime"), endDateTime=dataRequest.getDate("endDateTime");
        String buyerId=dataRequest.getString("buyerId"), sellerId=dataRequest.getString("sellerId");
        String logisticsStatus=dataRequest.getString("logisticsStatus");
        String orderStatus=dataRequest.getString("orderStatus");
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_QUERY))return CommonMethod.getReturnMessageError("无查询权限！");

        String startDateString="", endDateString="";
        if(endDateTime!=null)endDateString=formatDate(endDateTime);
        if(startDateTime!=null)startDateString=formatDate(startDateTime);

        String[] initArgs={startDateString,endDateString,buyerId,sellerId,logisticsStatus,orderStatus};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_QUERY),"搜索交易");
        logRepository.save(log);
        try {
            String JSONString= search("searchOrders",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return CommonMethod.getReturnData(new JSONParser().parse(JSONString));
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return CommonMethod.getReturnMessageError(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createTxn(DataRequest dataRequest) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_ADD))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无创建权限");
//        OrderID string `json:"orderId"`
//        Name         string  `json:"name"`
//        Quantity     int     `json:"quantity"`
//        Amount       float64 `json:"amount"`
//        OrderTime    string  `json:"orderTime"`
//        BuyerID      string  `json:"buyerId"`
//        SellerID     string  `json:"sellerId"`
        String name=dataRequest.getString("name");
        if(dataRequest.getBoolean("encrypt"))name= MobileShieldEncInfo.encrypt(name);
        String[] initArgs={dataRequest.getString("orderId"),name,dataRequest.getString("quantity"),
                            dataRequest.getString("amount"),formatDate(dataRequest.getDate("orderTime")),
                            dataRequest.getString("buyerId"),dataRequest.getString("sellerId")};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_ADD),"创建交易");
        logRepository.save(log);
        try{
            invoke("createOrder",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("数据已上链");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateTxn(DataRequest dataRequest) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_UPDATE_STATUS))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无更改状态权限");
        String orderId=dataRequest.getString("orderId"), status=dataRequest.getString("status");
        String[] initArgs={orderId,status};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_UPDATE_STATUS),"修改交易："+orderId+" 为状态："+status);
        logRepository.save(log);
        try{
            invoke("updateOrder",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("状态更新已保存到区块链");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteTxn(String id) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_DELETE))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无删除权限");
        String[] initArgs={id};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_DELETE),"删除交易："+id);
        logRepository.save(log);
        try{
            invoke("deleteOrder",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("交易已删除");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateLogistics(DataRequest dataRequest) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_UPDATE_LOGISTICS))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无更改物流状态权限");
        String orderId=dataRequest.getString("orderId"), status=dataRequest.getString("logistics");
        String[] initArgs={orderId,status};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_UPDATE_LOGISTICS),"修改交易："+orderId+" 物流状态为："+status);
        logRepository.save(log);
        try{
            invoke("updateLogistics",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("物流状态已更新到区块链");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addBuyerReview(DataRequest dataRequest) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_UPDATE_BUYER))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无添加买家评价权限");
        String orderId=dataRequest.getString("orderId");
        String[] initArgs={orderId,dataRequest.getString("buyerReview")};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_UPDATE_BUYER),"添加买家评价："+orderId);
        logRepository.save(log);
        try{
            invoke("addBuyerReview",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("买家评价已更新到区块链");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addSellerReview(DataRequest dataRequest) {
        if(!sharedServiceUtil.hasRight(ERightType.RIGHT_UPDATE_SELLER))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无添加卖家评价权限");
        String orderId=dataRequest.getString("orderId");
        String[] initArgs={dataRequest.getString("orderId"),dataRequest.getString("sellerReview")};
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_UPDATE_SELLER),"添加卖家评价："+orderId);
        logRepository.save(log);
        try{
            invoke("addSellerReview",initArgs);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("卖家评价已更新区块链");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public DataResponse decryptName(String encrypted) {
        try {
            return CommonMethod.getReturnData(MobileShieldEncInfo.decrypt(encrypted));
        }catch (Exception e){
            return CommonMethod.getReturnMessageError("当前名称未加密！");
        }
    }

    private void invoke(String fcnName,String[] initArgs) throws Exception {
        UserContext userContext = new UserContext("name","李伟","Org1","Org1MSP");
        Enrollment enrollment = UserUtils.getEnrollment(path(keyFolderPathOrg1), keyFileNameOrg1, path(certFolderPathOrg1), certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        List<Peer> peers = getPeers(fabricClient);
        Orderer order = fabricClient.getOrderer("orderer.example.com", "grpcs://orderer.example.com:7050", path(tlsOrderFilePath));
        //String initArgs[] = {"114514", "5090Ti","100","0.5","2023.06.27","001","002",sm2PublicKey};
        fabricClient.invoke("mychannel", TransactionRequest.Type.GO_LANG, "orderManage",
                order, peers, fcnName, initArgs);
    }

    private String search(String fcnName,String[] initArgs) throws Exception {
        UserContext userContext = new UserContext("name","李伟","Org1","Org1MSP");
        Enrollment enrollment = UserUtils.getEnrollment(path(keyFolderPathOrg1), keyFileNameOrg1, path(certFolderPathOrg1), certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        List<Peer> peers = getPeers(fabricClient);
        Map map = fabricClient.queryChaincode(peers, "mychannel", TransactionRequest.Type.GO_LANG, "orderManage", fcnName, initArgs);
        String JSONString="";
        for(Object key:map.keySet())JSONString= (String) map.get(key);
        return JSONString;
    }

    private List<Peer> getPeers(FabricClient fabricClient) throws InvalidArgumentException, IOException {
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", path(tlsPeerFilePath));
        Peer peer1 = fabricClient.getPeer("peer0.org2.example.com", "grpcs://peer0.org2.example.com:9051", path(tlsPeerFilePathAddition2));
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        return peers;
    }

    private String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }


    private User getCurrentUser(){
        Integer userId = CommonMethod.getUserId();
        User user = userRepository.findByUserId(userId).get();
        return user;
    }

    private String path(String filePath) throws IOException {
        return resourceLoader.getResource("classpath:"+filePath).getFile().getAbsolutePath();
    }
    @Value("${path.keyFolderPathOrg1}")
    String keyFolderPathOrg1;
    @Value("${path.keyFileNameOrg1}")
    String keyFileNameOrg1;
    @Value("${path.certFolderPathOrg1}")
    String certFolderPathOrg1;
    @Value("${path.certFileNameOrg1}")
    String certFileNameOrg1;
    @Value("${path.tlsPeerFilePath}")
    String tlsPeerFilePath;
    @Value("${path.tlsPeerFilePathAddition2}")
    String tlsPeerFilePathAddition2;
    @Value("${path.tlsOrderFilePath}")
    String tlsOrderFilePath;
}
