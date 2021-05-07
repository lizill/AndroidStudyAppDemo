package com.example.studyapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONTask extends AsyncTask<String, String, String> {

    private JSONObject jsonObject;
    private String Path = "";
    private String method = "";

    public JSONTask(JSONObject jsonObject, String Path, String method) {
        this.jsonObject = jsonObject;
        this.Path = Path;
        this.method = method;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://127.0.0.1:3000/" + Path);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod(method);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");

                if(method.equals("POST")) {
                    con.setDoOutput(true);
                }
                con.setDoInput(true);

                con.connect();

                if(method.equals("POST")) {
                    OutputStream outputStream = con.getOutputStream();

                    // 서버로 데이터를 보내기 위한 버퍼
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();
                }

                // 서버로부터 데이터를 받기 위한 버퍼
                InputStream inputStream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString(); // 결과값을 리턴해줌
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null) con.disconnect();
                try {
                    if(reader != null) reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}