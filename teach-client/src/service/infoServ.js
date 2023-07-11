import {getRequest} from "@/service/templateRequest";

function getBasicRightList(){
    return getRequest('/api/info/getRightTypeList',null)
}

export {
    getBasicRightList
}
