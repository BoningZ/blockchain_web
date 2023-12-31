package com.baling.payload.request;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class DataRequest {
    private Map data;

    public DataRequest() {
        data = new HashMap();
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public void add(String key, Object obj){
        data.put(key,obj);
    }
    public Object get(String key){
        return data.get(key);
    }
    public String getString(String key){
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof String)
            return (String)obj;
        return obj.toString();
    }
    public Boolean getBoolean(String key){
        Object obj = data.get(key);
        if(obj == null)
            return false;
        if(obj instanceof Boolean)
            return (Boolean)obj;
        if("true".equals(obj.toString()))
            return true;
        else
            return false;
    }

    public List getList(String key){
        Object obj = data.get(key);
        if(obj == null)
            return new ArrayList();
        if(obj instanceof List)
            return (List)obj;
        else
            return new ArrayList();
    }
    public Map getMap(String key){
        Object obj = data.get(key);
        if(obj == null)
            return new HashMap();
        if(obj instanceof Map)
            return (Map)obj;
        else
            return new HashMap();
    }

    public Integer getInteger(String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Integer)
            return (Integer)obj;
        String str = obj.toString();
        try {
            return new Integer(str);
        }catch(Exception e) {
            return null;
        }
    }
    public Long getLong(String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Long)
            return (Long)obj;
        String str = obj.toString();
        try {
            return new Long(str);
        }catch(Exception e) {
            return null;
        }
    }

    public Double getDouble(String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Double)
            return (Double)obj;
        String str = obj.toString();
        try {
            return new Double(str);
        }catch(Exception e) {
            return null;
        }
    }
    public Date getDate(String key) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof String){
            try{
                try{
                    //vue的奇妙bug
                    return addOneDay(dateFormat1.parse((String) obj));
                }catch (Exception e){
                    return addOneDay(dateFormat2.parse((String) obj));
                }
            }catch (Exception e){
                return null;
            }
        }
        else
            return null;
    }

    private Date addOneDay(Date date){
        return DateUtils.truncate(DateUtils.addDays(date, 1), Calendar.DATE);
    }
    public Date getTime(String key) {
        return null;
    }

}
