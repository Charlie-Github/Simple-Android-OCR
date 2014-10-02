package com.edible.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;


/**
 * 将HttpResponse中的JSONObject类型的Entity转为Java类
 * @author mingjiang
 *
 * @param <T>
 */
public class MyResponseHandler implements ResponseHandler<String> {
	
	@Override
	public String handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		StatusLine statusLine = response.getStatusLine();
        //statusCode >= 300表示请求需要进行重定向或错误(4xx -客户端错误, 5xx -服务端错误)
		System.out.println("STATUS:" + statusLine.getStatusCode());
		if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpResponseException(
                    statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        } else {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            try {
            	String s = "";
            	StringBuilder sb = new StringBuilder();
            	while((s = reader.readLine()) != null) {
            		sb.append(s);
            	}
            	String resp = sb.toString();
            	System.out.println(resp);
            	return resp;
            } finally {
            	reader.close();
            }
        }
	}

}
