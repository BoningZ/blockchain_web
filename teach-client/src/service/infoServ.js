import {getRequest} from "@/service/templateRequest";

function getBasicRightList(){
    return getRequest('/api/info/getRightTypeList',null)
}

function getAllRightList(){
    return getRequest('/api/info/getAllRightTypeList',null)
}

function getLogisticsList(){
    return getRequest('/api/info/getLogisticsStatus',null)
}
function getOrderStatusList(){
    return getRequest('/api/info/getOrderStatus',null)
}

export {
    getBasicRightList,
    getAllRightList,
    getLogisticsList,
    getOrderStatusList
}
