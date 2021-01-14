package com.walcron.lejos.api;

import java.net.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

public class JsonHttpClient {
  private final String url;
  private final String method;
  private final int connectionTimeout;

  public JsonHttpClient() {
    this.url = Const.URL;
    this.method = Const.METHOD;
    this.connectionTimeout = Const.CONNECTION_TIMEOUT_IN_SECONDS * 1000;
  }

  public void sendAck(String message) {
	try {  
	    if(url != null & method != null) {
	    	String jsonInputString = "{\"message\": \"" + message + "\"}";
	    	
	    	URL url = new URL(this.url);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
		    con.setDoOutput(true);
		    con.setRequestMethod(this.method);
		    con.setRequestProperty("Content-Type", "application/json; utf-8");
		    con.setRequestProperty("Accept", "application/json") ;
		    con.setConnectTimeout(connectionTimeout);
		    con.setRequestProperty("Content-Length", Integer.toString(jsonInputString.length())) ;
		
	      try(DataOutputStream os = new DataOutputStream(con.getOutputStream())) {
	        byte[] input = jsonInputString.getBytes("utf-8");
	        os.write(input, 0, input.length);
	        os.flush();
	        os.close();
	      }
	      catch(IOException ioe) {
	        System.err.print("write error:"+ioe.getMessage());
	      }
	      
	      //No response wait, expect httpstatus 204. But this is getInput stream is required to be called!
	      con.getInputStream().close();

	      System.out.println("Message Sent.");
	      con.disconnect();
	    }
	} catch(IOException e) {
		System.err.print(e.getMessage());
	}
  }
}
