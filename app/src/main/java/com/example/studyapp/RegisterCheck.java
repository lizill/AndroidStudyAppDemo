package com.example.studyapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterCheck extends StringRequest {

    final static private String URL = "https://www.dong0110.com/chatphp/RegisterCheck.php";
    private Map<String, String> parameters;

    public RegisterCheck(String userID, Response.Listener<String> listner) {
        super(Method.POST, URL, listner, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}