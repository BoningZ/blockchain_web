import axios from "axios";
import { store } from "@/store/createStore.js"

function postRequest(url, data) {
    return axios.post(
        url,{
            data: data
        },{
            headers: {
                Authorization: 'Bearer ' + store.state.jwtToken
            }
        }).then(res => {
        return res.data.data
    })
}

function getRequest(url,params){
    return axios.get(
        url,{
            params:params,
            headers: {
                Authorization: 'Bearer ' + store.state.jwtToken
            }
        }).then(res=>{
        return res.data.data
    })
}

function putRequest(url,data){
    return axios.put(
        url,{
            data: data
        },{
            headers: {
                Authorization: 'Bearer ' + store.state.jwtToken
            }
        }).then(res => {
        return res
    })
}

function deleteRequest(url,params){
    return axios.delete(
        url,{
            params:params,
            headers: {
                Authorization: 'Bearer ' + store.state.jwtToken
            }
        }).then(res => {
        return res
    })
}

export {
    putRequest,
    postRequest,
    getRequest,
    deleteRequest
}
