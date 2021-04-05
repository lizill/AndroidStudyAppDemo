package com.example.studyapp.ui.group;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LeaveGroupRequest extends StringRequest {
    final static private String URL = "https://www.dong0110.com/chatphp/LeaveGroup.php";
    private Map<String, String> parameters;

    public LeaveGroupRequest(String userID, String group, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("group", group);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
