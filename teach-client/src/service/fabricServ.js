import {putRequest,postRequest,getRequest,deleteRequest} from "./templateRequest"

function createOrder(data){
    return putRequest('/api/chainData/createTxn',data);
}

function deleteOrder(params){
    return deleteRequest('/api/chainData/deleteTxn',params)
}

function multiDeleteOrder(data){
    return postRequest('/api/chainData/deleteTxs',data)
}

function searchOrder(data){
    return postRequest('/api/chainData/searchTxs',data)
}

function getHistoryOrder(params){
    return getRequest('/api/chainData/getTxnHistory',params)
}

function updateTxn(data){
    return putRequest('/api/chainData/updateTxn',data)
}

function updateLogistics(data){
    return putRequest('/api/chainData/updateLogistics',data)
}

function addBuyerReview(data){
    return putRequest('/api/chainData/addBuyerReview',data)
}

function addSellerReview(data){
    return putRequest('/api/chainData/addSellerReview',data)
}

function decrypt(data){
    return postRequest('/api/chainData/decrypt',data)
}

function translate(data){
    return postRequest('/api/chainData/translate',data)
}

export {
    createOrder,
    deleteOrder,
    multiDeleteOrder,
    searchOrder,
    getHistoryOrder,
    updateTxn,
    updateLogistics,
    addBuyerReview,
    addSellerReview,
    decrypt,
    translate
}
