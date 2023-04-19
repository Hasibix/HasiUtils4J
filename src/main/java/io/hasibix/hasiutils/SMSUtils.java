package io.hasibix.hasiutils;

import java.io.IOException;
import java.io.InputStream;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;

public class SMSUtils {
    private static String ApiKey;
    private static final HttpTransport client = HttpRequestSender.NewClient();

    public static void Intialize(String apiKey) {
        ApiKey = apiKey;
    }

    public static InputStream SendMessage(String message, String sender, String number) {
		String urlString = String.format("%s?%s&%s&%s&%s",
            "https://api.txtlocal.com/send/",
            "apikey=" + new UrlEncodedContent(ApiKey, true),
            "&message=" + new UrlEncodedContent(message, true).toString(),
            "&sender=" + new UrlEncodedContent(sender, true).toString(),
            "&numbers=" + new UrlEncodedContent(number, true).toString()
        );
        InputStream result;

        try {
            result = HttpRequestSender.SendAsync(client.createRequestFactory().buildGetRequest(new GenericUrl(urlString))).getContent();
        } catch (IOException e) {
            result = null;
        }

        return result;
    }
}
