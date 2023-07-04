package com.baling.sdk;

import org.hyperledger.fabric.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SdkMain {

    private static final Logger log = LoggerFactory.getLogger(SdkMain.class);

    // 组织1  admin用户的密钥路径
    private static final String keyFolderPathOrg1 = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\users\\Admin@org1.example.com\\msp\\keystore";
    // 组织1  admin用户的密钥文件
    private static final String keyFileNameOrg1 = "5fbff5b11b8e59f2df61d9cf5fe99fd56b7b382579199d4556f2386e2373d3aa_sk";
    // 组织1  admin用户的证书路径
    private static final String certFoldePathOrg1 = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\users\\Admin@org1.example.com\\msp\\admincerts";
    // 组织1  admin用户的证书
    private static final String certFileNameOrg1 = "Admin@org1.example.com-cert.pem";

    // 组织2  admin用户的密钥路径
    private static final String keyFolderPathOrg2 = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org2.example.com\\users\\Admin@org2.example.com\\msp\\keystore";
    // 组织2  admin用户的密钥文件
    private static final String keyFileNameOrg2 = "581439641061f7919e7b3ce48f326553a6b05adf108312e0ed111d3b79064f0c_sk";
    // 组织2  admin用户的证书路径
    private static final String certFoldePathOrg2 = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org2.example.com\\users\\Admin@org2.example.com\\msp\\admincerts";
    // 组织2  admin用户的证书
    private static final String certFileNameOrg2 = "Admin@org2.example.com-cert.pem";

    // order tlsca证书
    private static final String tlsOrderFilePath = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\ordererOrganizations\\example.com\\tlsca\\tlsca.example.com-cert.pem";
    // tx文件
    private static final String txfilePath = "D:\\Java_demo\\fabric-test\\src\\main\\resources\\test.tx";
    //
    private static final String tlsPeerFilePath = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\peers\\peer0.org1.example.com\\msp\\tlscacerts\\tlsca.org1.example.com-cert.pem";

    // 组织1 tlsca
    private static final String tlsPeerFilePathAddtion1 = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\tlsca\\tlsca.org1.example.com-cert.pem";

    // 组织2 tlsca
    private static final String tlsPeerFilePathAddtion2 = "D:\\Java_demo\\fabric-test\\fabric-test\\src\\main\\resources\\crypto-config\\peerOrganizations\\org2.example.com\\tlsca\\tlsca.org2.example.com-cert.pem";

    private static final String yamlPath= "E:\\sansec\\chaincode\\src\\orderManage\\chaincodeendorsementpolicy.yaml";

//    private static final String sm2PrivateKey="f6e25195d03aea1ab7a6d83043a613a48ebcecfa94a1f0c3b8c1769b1c0a1fd8";
//    private static final String sm2PublicKey="b15a259ddf990a5c69cfd8ec638ff7823e4a7683c88fb724c02f4326d8f71529";

    public static void main(String[] args) {
        try {

            installChaincodeOrg1();
            installChaincodeOrg2();

            initChaincode();

            upgradeChaincode();

            //合约触发
            //"createOrder", {"114514", "5090Ti","100","0.5","2023.06.27","001","002"}
            //"updateOrder", {"114514", "Accepted"}
            //"addBuyerReview", {"114514", "Good Mining Tool"}
            //"addSellerReview", {"114514", "Thanks"}
//            String initArgs[]= {"5141140", "5060Ti","200","0.4","2023.07.02","003","002"};
//            invoke("createOrder", initArgs);
//            String initArgs[]={"114514"};
//            queryChaincode("getOrderHistory",initArgs);
            //invoke("searchOrders",initArgs);


            // 查询合约
//            queryChaincode();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 创建channel并将节点加入通道
//    public static void createChannelJoinPeer() throws Exception {
//        UserContext userContext = new UserContext();
//        userContext.setAffiliation("Org1");
//        userContext.setMspId("Org1MSP");
//        userContext.setAccount("李伟");
//        userContext.setName("admin");
//        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFoldePathOrg1, certFileNameOrg1);
//        userContext.setEnrollment(enrollment);
//        FabricClient fabricClient = new FabricClient(userContext);
////        Channel channel = fabricClient.getChannel("test");
////        channel.addOrderer(fabricClient.getOrderer("orderer.example.com","grpcs://orderer.example.com:7050",tlsOrderFilePath));
//
//        Channel channel = fabricClient.createChannel("test", fabricClient.getOrderer("orderer.example.com", "grpcs://orderer.example.com:7050", tlsOrderFilePath), txfilePath);
//        channel.joinPeer(fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", tlsPeerFilePath));
//        channel.initialize();
//    }

    //组织1安装合约
    //安装成功后在Ubuntu虚机里，进入docker虚机cli，执行命令：peer chaincode list --installed ，查看已安装好的合约
    public static void installChaincodeOrg1() throws Exception {
//        ChaincodeStub stub = ChaincodeStub;
        List list = new ArrayList();
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFoldePathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", tlsPeerFilePathAddtion1);
        Peer peer1 = fabricClient.getPeer("peer1.org1.example.com", "grpcs://peer1.org1.example.com:8051", tlsPeerFilePathAddtion1);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        fabricClient.installChaincode(TransactionRequest.Type.GO_LANG, "orderManage", "2.2",
                "D:\\Java_demo\\fabric-test\\teach-server\\src\\main\\resources\\chaincode", "orderManage", peers);
    }

    //组织2安装合约
    //安装成功后在Ubuntu虚机里，进入docker虚机cli，执行命令：peer chaincode list --installed ，查看已安装好的合约
    public static void installChaincodeOrg2() throws Exception {
//        ChaincodeStub stub = ChaincodeStub;
        List list = new ArrayList();
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org2");
        userContext.setMspId("Org2MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg2, keyFileNameOrg2, certFoldePathOrg2, certFileNameOrg2);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org2.example.com", "grpcs://peer0.org2.example.com:9051", tlsPeerFilePathAddtion2);
        Peer peer1 = fabricClient.getPeer("peer1.org2.example.com", "grpcs://peer1.org2.example.com:10051", tlsPeerFilePathAddtion2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        fabricClient.installChaincode(TransactionRequest.Type.GO_LANG, "orderManage", "2.2",
                "D:\\Java_demo\\fabric-test\\teach-server\\src\\main\\resources\\chaincode", "orderManage", peers);
    }

    //合约实例化
    //安装成功后在Ubuntu虚机里，进入docker虚机cli，执行命令：peer chaincode list --instantiated -C mychannel ，查看已实例化好的合约
    public static void initChaincode() throws Exception {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFoldePathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", tlsPeerFilePath);
        Orderer order = fabricClient.getOrderer("orderer.example.com", "grpcs://orderer.example.com:7050", tlsOrderFilePath);
        String initArgs[] = {""};
        fabricClient.initChaincode("mychannel", TransactionRequest.Type.GO_LANG, "orderManage",
                "2.2", order, peer, "init", initArgs);
    }

    /**
     * 合约升级
     * 执行合约升级前，需要先安装待升级版本的合约，比如如果要升级成2.0版本，则需要先执行2.0版本的合约安装
     */

    public static void upgradeChaincode() throws Exception {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFoldePathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", tlsPeerFilePath);
        Orderer order = fabricClient.getOrderer("orderer.example.com", "grpcs://orderer.example.com:7050", tlsOrderFilePath);
        String initArgs[] = {""};
        fabricClient.upgradeChaincode("mychannel", TransactionRequest.Type.GO_LANG, "orderManage",
                "2.2", order, peer, "init", yamlPath, initArgs);
    }

    /**
     * invoke 合约
     * invoke执行完成后，进入cli虚机，执行如下命令查询合约内容
     *  peer chaincode query -C mychannel -n basicinfo -c '{"Args":["query","110114"]}'
     * @throws Exception
     */
    public static void invoke(String fcnName,String[] initArgs) throws Exception {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFoldePathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", tlsPeerFilePath);
        Peer peer1 = fabricClient.getPeer("peer0.org2.example.com", "grpcs://peer0.org2.example.com:9051", tlsPeerFilePathAddtion2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        Orderer order = fabricClient.getOrderer("orderer.example.com", "grpcs://orderer.example.com:7050", tlsOrderFilePath);
        //String initArgs[] = {"114514", "5090Ti","100","0.5","2023.06.27","001","002",sm2PublicKey};
        fabricClient.invoke("mychannel", TransactionRequest.Type.GO_LANG, "orderManage",
                order, peers, fcnName, initArgs);
    }

    /**
     * 查询合约
     *
     * @throws Exception
     */
    public static void queryChaincode(String fcnName,String[] initArgs) throws Exception {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFoldePathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051", tlsPeerFilePath);
        Peer peer1 = fabricClient.getPeer("peer0.org2.example.com", "grpcs://peer0.org2.example.com:9051", tlsPeerFilePathAddtion2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        //String initArgs[] = {"114514",sm2PrivateKey};

        Map map = fabricClient.queryChaincode(peers, "mychannel", TransactionRequest.Type.GO_LANG, "orderManage", fcnName, initArgs);
        System.out.println(map);
    }

    //注册用户 hqCZUStrRTAR
    public static void register() throws Exception {
        FabricCAClient caClient = new FabricCAClient("http://192.168.70.43", null);
        UserContext register = new UserContext();
        register.setName("lihua");
        register.setAffiliation("org2");
        Enrollment enrollment = caClient.enroll("admin", "adminpw");
        UserContext registar = new UserContext();
        registar.setName("admin");
        registar.setAffiliation("org2");
        registar.setEnrollment(enrollment);
        String secret = caClient.register(registar, register);
        System.out.println(secret);
    }

    //注册用户查询合约
    public static void queryChaincode2() throws Exception{
        FabricCAClient caClient = new FabricCAClient("http://192.168.70.43",null);
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org2");
        userContext.setMspId("Org2MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = caClient.enroll("lihua","hqCZUStrRTAR");
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com","grpcs://peer0.org1.example.com:7051",tlsPeerFilePath);
        Peer peer1 = fabricClient.getPeer("peer0.org2.example.com","grpcs://peer0.org2.example.com:9051",tlsPeerFilePathAddtion2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        String initArgs[] = {"110120"};
        Map map =  fabricClient.queryChaincode(peers,"mychannel", TransactionRequest.Type.GO_LANG,"basicinfo","query",initArgs);
        System.out.println(map);
    }


    //注册用户invoke合约
    public static void invoke2() throws Exception{
        FabricCAClient caClient = new FabricCAClient("http://192.168.70.43",null);
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org2");
        userContext.setMspId("Org2MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment = caClient.enroll("lihua","hqCZUStrRTAR");
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.example.com","grpcs://peer0.org1.example.com:7051",tlsPeerFilePath);
        Peer peer1 = fabricClient.getPeer("peer0.org2.example.com","grpcs://peer0.org2.example.com:9051",tlsPeerFilePathAddtion2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        Orderer order = fabricClient.getOrderer("orderer.example.com","grpcs://orderer.example.com:7050",tlsOrderFilePath);
        String initArgs[] = {"110120","{\"name\":\"zhangsan\",\"identity\":\"110120\",\"mobile\":\"18910012222\"}"};
        fabricClient.invoke("mychannel", TransactionRequest.Type.GO_LANG,"basicinfo",order,peers,"save",initArgs);
    }
}
