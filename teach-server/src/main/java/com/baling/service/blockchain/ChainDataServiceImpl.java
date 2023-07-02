package com.baling.service.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.sdk.FabricClient;
import com.baling.sdk.UserContext;
import com.baling.sdk.UserUtils;
import com.baling.util.CommonMethod;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ChainDataServiceImpl implements ChainDataService{
    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public DataResponse getTxnById(String id) {
        try {
            UserContext userContext = new UserContext("name","李伟","Org1","Org1MSP");
            Enrollment enrollment = UserUtils.getEnrollment(path(keyFolderPathOrg1), keyFileNameOrg1, path(certFolderPathOrg1), certFileNameOrg1);
            userContext.setEnrollment(enrollment);
            FabricClient fabricClient = new FabricClient(userContext);
            List<Peer> peers = getPeers(fabricClient);
            String[] initArgs = {id};
            Map map = fabricClient.queryChaincode(peers, "mychannel", TransactionRequest.Type.GO_LANG, "orderManage", "getOrder", initArgs);
            String JSONString="";
            for(Object key:map.keySet())JSONString= (String) map.get(key);
            JSONObject jsonObject= (JSONObject) new JSONParser().parse(JSONString);
            return CommonMethod.getReturnData(jsonObject);
        }catch (Exception e){
            return CommonMethod.getReturnMessageError(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createTxn(DataRequest dataRequest) {
//        OrderID string `json:"orderId"`
//        Name         string  `json:"name"`
//        Quantity     int     `json:"quantity"`
//        Amount       float64 `json:"amount"`
//        OrderTime    string  `json:"orderTime"`
//        BuyerID      string  `json:"buyerId"`
//        SellerID     string  `json:"sellerId"`
        String[] initArgs={dataRequest.getString("orderId"),dataRequest.getString("name"),dataRequest.getString("quantity"),
                            dataRequest.getString("amount"),dataRequest.getString("orderTime"),
                            dataRequest.getString("buyerId"),dataRequest.getString("sellerId")};
        try{
            invoke("createOrder",initArgs);
            return ResponseEntity.ok("created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateTxn(DataRequest dataRequest) {
        String[] initArgs={dataRequest.getString("orderId"),dataRequest.getString("status")};
        try{
            invoke("updateOrder",initArgs);
            return ResponseEntity.ok("updated");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addBuyerReview(DataRequest dataRequest) {
        String[] initArgs={dataRequest.getString("orderId"),dataRequest.getString("buyerReview")};
        try{
            invoke("addBuyerReview",initArgs);
            return ResponseEntity.ok("added");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addSellerReview(DataRequest dataRequest) {
        String[] initArgs={dataRequest.getString("orderId"),dataRequest.getString("sellerReview")};
        try{
            invoke("addSellerReview",initArgs);
            return ResponseEntity.ok("added");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private void invoke(String fcnName,String[] initArgs) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CryptoException, InvalidArgumentException, org.hyperledger.fabric.sdk.exception.CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, TransactionException, ProposalException {
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

    private List<Peer> getPeers(FabricClient fabricClient) throws InvalidArgumentException, IOException {
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", path(tlsPeerFilePath));
        Peer peer1 = fabricClient.getPeer("peer0.org2.example.com", "grpcs://peer0.org2.example.com:9051", path(tlsPeerFilePathAddition2));
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        return peers;
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
