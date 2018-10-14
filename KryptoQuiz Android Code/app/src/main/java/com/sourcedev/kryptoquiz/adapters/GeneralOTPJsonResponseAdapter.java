package com.sourcedev.kryptoquiz.adapters;

import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

class GenerateOTPJson {
    String message;
    Boolean status;
}

public class GeneralOTPJsonResponseAdapter {

    @FromJson
    GeneralResponse generalResponseJsonResponseFromJson(GenerateOTPJson apiJson) {
        GeneralResponse entity = new GeneralResponse();
        entity.setMessage(apiJson.message);
        entity.setStatus(apiJson.status);
        return entity;
    }

    @ToJson
    GenerateOTPJson generatepiResponseToJson(GeneralResponse entity) {
        GenerateOTPJson json = new GenerateOTPJson();
        json.message = entity.getMessage();
        json.status = entity.getStatus();
        return json;
    }
}