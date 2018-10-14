package com.sourcedev.kryptoquiz.adapters;

import com.sourcedev.kryptoquiz.models.VerifyOtpResponse;
import com.squareup.moshi.FromJson;

class VerifyOtpResponseJson{
    Boolean status;
    String message;
    String token;
    Boolean newUser;
}

public class VerifyOtpResponseJsonAdapter {

    @FromJson
    VerifyOtpResponse ResponseFromJson(VerifyOtpResponseJson json){
        VerifyOtpResponse v = new VerifyOtpResponse();
        v.setMsg(json.message);
        v.setStatus(json.status);
        v.setNewUser(json.newUser);
        v.setUserToken(json.token);
        return v;
    }

}