package io.hasibix.hasiutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestSender {
	private static final Logger LOGGER = Logger.of(HttpRequestSender.class);

	public static HttpURLConnection openConnection(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		return connection;
	}

	public static String sendRequest(URL url) {
		try {
			HttpURLConnection connection = openConnection(url);
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = reader.readLine()) != null) {
					response.append(inputLine);
				}
				reader.close();
				inputStream.close();
				return response.toString();
			} else {
				LOGGER.error("An error occurred while sending an HTTP request. Response code: " + responseCode);
				return null;
			}
		} catch (IOException e) {
			LOGGER.error("An exception occurred while sending an HTTP request");
			LOGGER.trace(e);
			return null;
		}
	}
}