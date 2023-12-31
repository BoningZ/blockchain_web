package com.baling.sdk;

import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.hyperledger.fabric.protos.common.Ledger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FabricClient {

    private static final Logger log = LoggerFactory.getLogger(FabricClient.class);

    private HFClient hfClient;

    public FabricClient(UserContext userContext)
            throws IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException,
            NoSuchMethodException, CryptoException, ClassNotFoundException {
        hfClient = HFClient.createNewInstance();
        // 定义加密算法
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        // 设置加密算法
        hfClient.setCryptoSuite(cryptoSuite);
        // 设置用户环境变量
        hfClient.setUserContext(userContext);
    }

    /**
     * @param channelName channel的名字
     * @param order       order的信息
     * @param txPath      创建channel所需的tx文件
     * @return Channel
     * @throws IOException
     * @throws InvalidArgumentException
     * @throws TransactionException
     * @description 创建channel
     */
    public Channel createChannel(String channelName, Orderer order, String txPath)
            throws IOException, InvalidArgumentException, TransactionException {
        ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(txPath));
        return hfClient.newChannel(channelName, order, channelConfiguration,
                hfClient.getChannelConfigurationSignature(channelConfiguration, hfClient.getUserContext()));
    }

    /**
     * @param lang              合约开发语言
     * @param chaincodeName     合约名称
     * @param chaincodeVersion  合约版本
     * @param chaincodeLocation 合约的目录路径
     * @param chaincodePath     合约的文件夹
     * @param peers             安装的peers 节点
     * @throws InvalidArgumentException
     * @throws ProposalException
     * @description 安装合约
     */
    public void installChaincode(TransactionRequest.Type lang, String chaincodeName, String chaincodeVersion,
                                 String chaincodeLocation, String chaincodePath, List<Peer> peers)
            throws InvalidArgumentException, ProposalException {
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        installProposalRequest.setChaincodeLanguage(lang);
        installProposalRequest.setChaincodeID(builder.build());
        installProposalRequest.setChaincodeSourceLocation(new File(chaincodeLocation));
        installProposalRequest.setChaincodePath(chaincodePath);
        Collection<ProposalResponse> responses = hfClient.sendInstallProposal(installProposalRequest, peers);
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} installed success", response.getPeer().getName());
            } else {
                log.error("{} installed fail", response.getMessage());
            }
        }
    }

    /**
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param chaincodeVersion
     * @param order
     * @param peer
     * @param funcName         合约实例化执行的函数
     * @param args             合约实例化执行的参数
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     * @description 合约的实例化
     */
    public void initChaincode(String channelName, TransactionRequest.Type lang, String chaincodeName,
                              String chaincodeVersion, Orderer order, Peer peer, String funcName, String args[])
            throws TransactionException, ProposalException, InvalidArgumentException {
        Channel channel = getChannel(channelName);
        channel.addPeer(peer);
        channel.addOrderer(order);
        channel.initialize();
        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setArgs(args);
        instantiateProposalRequest.setFcn(funcName);
        instantiateProposalRequest.setChaincodeLanguage(lang);
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        instantiateProposalRequest.setChaincodeID(builder.build());
        Collection<ProposalResponse> responses = channel.sendInstantiationProposal(instantiateProposalRequest);
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} init success", response.getPeer().getName());
            } else {
                log.error("{} init fail", response.getMessage());
            }
        }
        channel.sendTransaction(responses);
    }

    /**
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param chaincodeVersion
     * @param order
     * @param peer
     * @param funcName
     * @param args
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     * @throws IOException
     * @throws ChaincodeEndorsementPolicyParseException
     * @description 合约的升级
     */
    public void upgradeChaincode(String channelName, TransactionRequest.Type lang, String chaincodeName,
                                 String chaincodeVersion, Orderer order, Peer peer, String funcName, String yamlPath,
                                 String args[])
            throws TransactionException, ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException {
        Channel channel = getChannel(channelName);
        channel.addPeer(peer);
        channel.addOrderer(order);
        channel.initialize();
        UpgradeProposalRequest upgradeProposalRequest = hfClient.newUpgradeProposalRequest();
        upgradeProposalRequest.setArgs(args);
        upgradeProposalRequest.setFcn(funcName);
        upgradeProposalRequest.setChaincodeLanguage(lang);
        // 设置背书策略
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File(yamlPath));
        upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        upgradeProposalRequest.setChaincodeID(builder.build());
        Collection<ProposalResponse> responses = channel.sendUpgradeProposal(upgradeProposalRequest);
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} upgrade success", response.getPeer().getName());
            } else {
                log.error("{} upgrade fail", response.getMessage());
            }
        }
        channel.sendTransaction(responses);
    }

    /**
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param order
     * @param peers
     * @param funcName      合约调用执行的函数名称
     * @param args          合约调用执行的参数
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     * @description 合约的调用
     */
    public void invoke(String channelName, TransactionRequest.Type lang, String chaincodeName, Orderer order,
                       List<Peer> peers, String funcName, String args[])
            throws Exception {
        Channel channel = getChannel(channelName);
        channel.addOrderer(order);
        for (Peer p : peers) {
            channel.addPeer(p);
        }
        channel.initialize();
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeLanguage(lang);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setFcn(funcName);
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName);
        transactionProposalRequest.setChaincodeID(builder.build());
        Collection<ProposalResponse> responses = channel.sendTransactionProposal(transactionProposalRequest, peers);
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} invoke proposal {} success", response.getPeer().getName(), funcName);
            } else {
                String logArgs[] = {response.getMessage(), funcName, response.getPeer().getName()};
                log.error("{} invoke proposal {} fail on {}", logArgs);
                throw new Exception(response.getMessage());
            }
        }
        channel.sendTransaction(responses);
    }

    /**
     * @param peers
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param funcName
     * @param args
     * @return
     * @throws TransactionException
     * @throws InvalidArgumentException
     * @throws ProposalException
     * @description 合约的查询
     */
    public Map queryChaincode(List<Peer> peers, String channelName, TransactionRequest.Type lang,
                              String chaincodeName, String funcName, String args[])
            throws Exception {
        Channel channel = getChannel(channelName);
        for (Peer p : peers) {
            channel.addPeer(p);
        }
        channel.initialize();
        HashMap map = new HashMap();
        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName);
        queryByChaincodeRequest.setChaincodeID(builder.build());
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(funcName);
        queryByChaincodeRequest.setChaincodeLanguage(lang);
        Collection<ProposalResponse> responses = channel.queryByChaincode(queryByChaincodeRequest);
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("data is {}", response.getProposalResponse().getResponse().getPayload());
                map.put(response.getStatus().getStatus(), new String(response.getProposalResponse().getResponse().getPayload().toByteArray()));
                return map;
            } else {
                log.error("data get error {}", response.getMessage());
                map.put(response.getStatus().getStatus(), response.getMessage());
                throw new Exception(response.getMessage());
            }
        }
        map.put("code", "404");
        return map;
    }


    /**
     * @param name
     * @param grpcUrl
     * @param tlsFilePath
     * @return
     * @throws InvalidArgumentException
     * @description 获取orderer节点
     */
    public Orderer getOrderer(String name, String grpcUrl, String tlsFilePath) throws InvalidArgumentException {
        Properties properties = new Properties();
        properties.setProperty("pemFile", tlsFilePath);
        // 增加配置信息
        Orderer orderer = hfClient.newOrderer(name, grpcUrl, properties);
        return orderer;
    }

    /**
     * @param name
     * @param grpcUrl
     * @param tlsFilePath
     * @return
     * @throws InvalidArgumentException
     * @description 获取peer节点
     */
    public Peer getPeer(String name, String grpcUrl, String tlsFilePath) throws InvalidArgumentException {
        Properties properties = new Properties();
        properties.setProperty("pemFile", tlsFilePath);
        Peer peer = hfClient.newPeer(name, grpcUrl, properties);
        return peer;
    }

    /**
     * @param channelName
     * @return
     * @throws InvalidArgumentException
     * @throws TransactionException
     * @throws ProposalException
     * @description 获取已有的channel
     */
    public Channel getChannel(String channelName) throws InvalidArgumentException, TransactionException, ProposalException {
        Channel channel = hfClient.newChannel(channelName);
        return channel;
    }
}
