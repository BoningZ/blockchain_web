import {putRequest,deleteRequest,postRequest} from "./templateRequest"

function addRight(data){
    return putRequest('/api/right/addRight',data)
}

function deleteRight(params){
    return deleteRequest('/api/right/deleteRight',params)
}

function updateRight(data){
    return putRequest('/api/right/updateRight',data)
}

function getRightList(data){
    return postRequest('/api/right/getRightList',data)
}

export {
    addRight,
    deleteRight,
    updateRight,
    getRightList
}
