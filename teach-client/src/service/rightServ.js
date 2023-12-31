import {putRequest,deleteRequest,postRequest,getRequest} from "./templateRequest"

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

function getRightsOfAll(params){
    return getRequest('/api/right/getRightsOfAll',params)
}

function updateRightsByMember(data){
    return putRequest('/api/right/updateRightsByMember',data)
}

function getRightsMap(){
    return getRequest('/api/right/getRightsMap',null)
}
function getMyRightTypes(){
    return getRequest('/api/right/getMyRightTypes',null)
}
function getMyRights(){
    return getRequest('/api/right/getMemberRights',{"page":0});
}

export {
    addRight,
    deleteRight,
    updateRight,
    getRightList,

    getRightsOfAll,
    updateRightsByMember,
    getRightsMap,

    getMyRightTypes,
    getMyRights
}
