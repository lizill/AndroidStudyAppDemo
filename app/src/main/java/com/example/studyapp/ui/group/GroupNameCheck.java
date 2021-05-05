package com.example.studyapp.ui.group;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MakeGroupCheck extends StringRequest {
    final static private String URL = "https://www.dong0110.com/chatphp/LeaveGroup.php";
    private Map<String, String> parameters;

    public MakeGroupCheck(String groupName, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("groupName", groupName);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
