import {getRequest} from "@/service/templateRequest";

function getBasicRightList(){
    return getRequest('/api/info/getRightTypeList',null)
}

function getAllRightList(){
    return getRequest('/api/info/getAllRightTypeList',null)
}

export {
    getBasicRightList,
    getAllRightList
}
