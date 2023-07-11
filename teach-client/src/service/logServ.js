import {putRequest,postRequest,getRequest} from "./templateRequest"

function searchLogs(data){
    return postRequest('/api/audit/searchLogs',data)
}

function audit(params){
    return getRequest('/api/audit/audit',params)
}

function generateHash(data){
    return putRequest('/api/audit/generateHash',data)
}

export {
    searchLogs,
    audit,
    generateHash
}
