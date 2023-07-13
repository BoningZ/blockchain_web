package com.baling.service.blockchain;
import com.baling.util.CommonMethod;
import com.baling.util.SharedServiceUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

@Service
public class TranslateServiceImpl implements TranslateService{
    @Autowired
    SharedServiceUtil sharedServiceUtil;

    @Override
    public DataResponse translate(DataRequest dataRequest) {
        String sourceLanguage=dataRequest.getString("source");
        String content=dataRequest.getString("content");
        String targetLanguage=sourceLanguage.equals("zh")?"en":"zh";
        try{
            String result=baiduTranslate(content,sourceLanguage,targetLanguage);
            return CommonMethod.getReturnData(result);
        }catch (Exception e){
            return CommonMethod.getReturnMessageError(e.getMessage());
        }
    }


    private static final String API_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final String APP_ID = "20230713001742665";
    private static final String SECRET_KEY = "0DT5XkpLUm_cF52tYiHL";

    public String baiduTranslate(String text, String sourceLang, String targetLang) {
        String salt = String.valueOf(System.currentTimeMillis());

        String requestUrl = generateRequestUrl(text, sourceLang, targetLang, salt);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
        String responseBody = responseEntity.getBody();
        TranslationResponse response = parseResponse(responseBody);

        // Process the response
        if (response != null && response.getError_code() == 0) {
            return response.getTrans_result().get(0).getDst();
        } else {
            return null;
        }
    }

    private String generateRequestUrl(String text, String sourceLang, String targetLang, String salt) {
        String sign = generateSign(text, salt);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", text);
        params.add("from", sourceLang);
        params.add("to", targetLang);
        params.add("appid", APP_ID);
        params.add("salt", salt);
        params.add("sign", sign);

        return API_URL + "?" + mapToQueryString(params);
    }

    private String generateSign(String text, String salt) {
        String signStr = APP_ID + text + salt + SECRET_KEY;
        return sharedServiceUtil.calculateMd5Hash(signStr);
    }

    private String mapToQueryString(MultiValueMap<String, String> params) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.toSingleValueMap().entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        // Remove the trailing "&"
        return query.substring(0, query.length() - 1);
    }
    private TranslationResponse parseResponse(String responseBody) {
        try {
            JsonNode rootNode = new ObjectMapper().readTree(responseBody);

            TranslationResponse response = new TranslationResponse();
            response.setFrom(rootNode.path("from").asText());
            response.setTo(rootNode.path("to").asText());
            response.setError_code(rootNode.path("error_code").asInt());

            List<TranslationResult> translationResults = new ArrayList<>();
            JsonNode transResultNode = rootNode.path("trans_result");
            if (transResultNode.isArray()) {
                for (JsonNode resultNode : transResultNode) {
                    TranslationResult translationResult = new TranslationResult();
                    translationResult.setSrc(resultNode.path("src").asText());
                    translationResult.setDst(resultNode.path("dst").asText());
                    translationResults.add(translationResult);
                }
            }
            response.setTrans_result(translationResults);

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static class TranslationResponse {
        private String from;
        private String to;
        private List<TranslationResult> trans_result;
        private Integer error_code;

        public List<TranslationResult> getTrans_result() {
            return trans_result;
        }
        public Integer getError_code() {
            return error_code;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public void setTrans_result(List<TranslationResult> trans_result) {
            this.trans_result = trans_result;
        }

        public void setError_code(Integer error_code) {
            this.error_code = error_code;
        }
    }

    private static class TranslationResult {
        private String src;
        private String dst;

        public String getDst() {
            return dst;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }
}
