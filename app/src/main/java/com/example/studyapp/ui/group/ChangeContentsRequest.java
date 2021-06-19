package com.example.studyapp.ui.group;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangeContentsRequest extends StringRequest {
    final static private String URL = "https://www.dong0110.com/chatphp/ChangeContents.php";
    private Map<String, String> parameters;

    public ChangeContentsRequest(String groupName, String contents, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("groupName", groupName);
        parameters.put("contents", contents);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
