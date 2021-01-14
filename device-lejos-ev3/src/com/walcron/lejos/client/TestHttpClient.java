package com.walcron.lejos.client;

import com.walcron.lejos.api.JsonHttpClient;

public class TestHttpClient {

	public TestHttpClient() {
		JsonHttpClient jsonHttpClient = new JsonHttpClient();
		jsonHttpClient.sendAck("OK");
	}

	public static void main(String args[]) {
		new TestHttpClient();
	}
}
