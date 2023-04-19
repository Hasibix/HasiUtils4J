package io.hasibix.hasiutils;

import java.util.concurrent.Future;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class HttpRequestSender {
    private static final Logger logger = Logger.of(HttpRequestSender.class);

    public static HttpTransport NewClient() {
        return new NetHttpTransport();
    }

    public static HttpResponse SendAsync(HttpRequest request) {
        HttpResponse result;
        try {
            Future<HttpResponse> response = request.executeAsync();
            while (!response.isDone()) {
                result = null;
            }
            result = response.get(); 
        } catch (Exception e) {
            logger.error("An exception occurred while sending an HTTP request");
            logger.trace(e);
            result = null;
        }
        return result;
	}

    public static HttpResponse Send(HttpRequest request) {
        HttpResponse result;
        try {
            HttpResponse response = request.execute();
            result = response;
        } catch (Exception e) {
            logger.error("An exception occurred while sending an HTTP request");
            logger.trace(e);
            result = null;
        }
        return result;
	}
}