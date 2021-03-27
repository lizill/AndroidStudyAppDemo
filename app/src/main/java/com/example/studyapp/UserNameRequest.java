package com.example.studyapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserNameRequest extends StringRequest {

    final static private String URL = "https://www.dong0110.com/chatphp/UserName.php";
    private Map<String, String> parameters;

    public UserNameRequest(String userName, String userID, Response.Listener<String> listner) {
        super(Method.POST, URL, listner, null);
        parameters = new HashMap<>();
        parameters.put("userName", userName);
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}