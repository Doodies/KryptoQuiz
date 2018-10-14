package com.sourcedev.kryptoquiz.adapters;

import com.sourcedev.kryptoquiz.models.QuestionResponse;
import com.sourcedev.kryptoquiz.models.VerifyOtpResponse;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;


class QuestionJson{
    String id;
    String ques;
    String quesImage;
    String o_one;
    String o_two;
    String o_three;
    String o_four;
    String ans;
    String createdAt;
    String updatedAt;

}

class CustomQuestion{
    Boolean status;
    QuestionJson message;
}
public class QuestionResponseAdapter{

    @FromJson
    QuestionResponse ResponseFromJson(CustomQuestion json){
        QuestionResponse v = new QuestionResponse();
        v.setQues(json.message.ques);
        v.setOptionOne(json.message.o_one);
        v.setOptionTwo(json.message.o_two);
        v.setOptionThree(json.message.o_three);
        v.setOptionFour(json.message.o_four);
        v.setAns(json.message.ans);
        return v;
    }
}
