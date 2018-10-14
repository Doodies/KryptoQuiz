package com.sourcedev.kryptoquiz.adapters;

import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.models.User;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.ToJson;


class CustomMessage{
    String first_name;
    String last_name;
    String email;
    String phone;
    String gamesPlayed;
    String amountWon;
    String balance;

}
class UserDetailJson{
    Boolean status;
    CustomMessage message;
}

public class UserDetailJsonAdapter {

    @FromJson
    User generalResponseJsonResponseFromJson(UserDetailJson apiJson) {
        User entity = new User();
        entity.setFirstName(apiJson.message.first_name);
        entity.setLastName(apiJson.message.last_name);
        entity.setEmail(apiJson.message.email);
        entity.setPhone(apiJson.message.phone);
        entity.setGamesPlayed(apiJson.message.gamesPlayed);
        entity.setAmountWon(apiJson.message.amountWon);
        entity.setBalance(apiJson.message.balance);
        return entity;
    }
}