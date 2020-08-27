package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    // 서버 url 설정(PHP 파일 연동)
    final static private String URL = "http://starbugks.dothome.co.kr/Login.php";
    private Map<String, String> map;

    public LoginRequest(String date, String drinkAmount, String memo, String Kcal, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("date",date);
        map.put("drinkAmount",drinkAmount);
        map.put("memo",memo);
        map.put("Kcal",Kcal);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
