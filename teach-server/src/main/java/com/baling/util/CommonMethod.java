package com.baling.util;

import com.baling.payload.response.DataResponse;
import com.baling.security.services.UserDetailsImpl;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CommonMethod {
    public static String dataHash16(String data) {
        byte[] b_data = data.getBytes(StandardCharsets.UTF_8);
        byte[] hash_32 = digest(b_data, 0, b_data.length);
        byte[] hash_16 = new byte[16];
        System.arraycopy(hash_32, 0, hash_16, 0, 16);
        return Base64.getEncoder().encodeToString(hash_16);
    }


    public static byte[] digest(byte[] in, int inOff, int len) {
        SM3Digest sm3 = new SM3Digest();
        sm3.update(in, inOff, len);
        byte[] result = new byte[32];
        sm3.doFinal(result, 0);
        return result;
    }

    public static DataResponse getReturnData(Object obj, String msg){
        Map data = new HashMap();
        data.put("data",obj);
        data.put("msg",msg);
        return new   DataResponse("0",data);
    }
    public static DataResponse getReturnMessage(String code, String msg){
        Map data = new HashMap();
        data.put("data",null);
        data.put("msg",msg);
        return new DataResponse(code,data);
    }
    public static  DataResponse getReturnData(Object obj){
        return getReturnData(obj,null);
    }
    public static DataResponse getReturnMessageOK(String msg){
        return getReturnMessage("0", msg);
    }
    public static DataResponse getReturnMessageOK(){
        return getReturnMessage("0", null);
    }
    public static DataResponse getReturnMessageError(String msg){
        return getReturnMessage("1", msg);
    }

    public static Integer getUserId(){
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails != null)
            return userDetails.getId();
        else
            return null;
    }

    public static String getNextNum3( String num) {
        String str;
        String prefix;
        if(num.length() == 3) {
            str = num;
            prefix= "";
        }
        else {
            str = num.substring(num.length() - 3, num.length());
            prefix = num.substring(0,num.length() - 3);
        }
        int c;
        if(str.charAt(0)=='0') {
            if(str.charAt(1)=='0') {
                c = str.charAt(2)-'0';
            }else {
                c = (str.charAt(1)-'0')*10 + str.charAt(2)-'0';
            }
        }else {
            c = (str.charAt(0)-'0')*100  +(str.charAt(1)-'0')*10 + str.charAt(2)-'0';
        }
        c++;
        if(c < 10) {
            return prefix+"00" + c;
        }else if(c < 100) {
            return prefix+"0" + c;
        }else {
            return prefix+ c;
        }
    }
    public static String getNextNum4( String num) {
        String str;
        String prefix;
        if(num.length() == 4) {
            str = num;
            prefix= "";
        }
        else {
            str = num.substring(num.length() - 4, num.length());
            prefix = num.substring(0,num.length() - 4);
        }
        int c;
        if(str.charAt(0)=='0') {
            if (str.charAt(1) == '0') {
                if (str.charAt(2) == '0') {
                    c = str.charAt(3) - '0';
                } else {
                    c = (str.charAt(2) - '0') * 10 + str.charAt(3) - '0';
                }
            } else {
                c = (str.charAt(1) - '0') * 100 + (str.charAt(2) - '0') * 10 + str.charAt(3) - '0';
            }
        }else {
            c = (str.charAt(0) - '0') * 1000 + (str.charAt(1) - '0') * 100 + (str.charAt(2) - '0') * 10 + str.charAt(3) - '0';
        }
        c++;
        if(c < 10) {
            return prefix+"000" + c;
        }else if(c < 100) {
            return prefix+"00" + c;
        }else if(c < 1000){
            return prefix + "0" + c;
        }else {
            return prefix+ c;
        }
    }

}
