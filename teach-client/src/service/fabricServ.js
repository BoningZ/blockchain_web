import {putRequest,postRequest,getRequest,deleteRequest} from "./templateRequest"

function createOrder(data){
    return putRequest('/api/chainData/createTxn',data);
}

function deleteOrder(params){
    return deleteRequest('/api/chainData/deleteTxn',params)
}

function multiDeleteOrder(params){
    return deleteRequest('/api/chainData/deleteTxs',params)
}

function searchOrder(data){
    return postRequest('/api/chainData/searchTxs',data)
}

function getHistoryOrder(params){
    return getRequest('/api/chainData/getTxnHistory',params)
}

export {
    createOrder,
    deleteOrder,
    multiDeleteOrder,
    searchOrder,
    getHistoryOrder

}
