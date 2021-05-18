package com.example.studyapp.ui.group;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MakeGroupRequest extends StringRequest {
    final static private String URL = "https://www.dong0110.com/chatphp/MakeGroupRequest.php";
    private Map<String, String> parameters;

    public MakeGroupRequest(String groupName, String contents, String category, int goalTime, String userName, int memberLimit, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("groupName", groupName);
        parameters.put("contents", contents);
        parameters.put("category", category);
        parameters.put("goalTime", ""+goalTime);
        parameters.put("userName", userName);
        parameters.put("memberLimit", ""+memberLimit);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
