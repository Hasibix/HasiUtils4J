package io.hasibix.hasiutils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.jetbrains.annotations.Nullable;

public class SMSUtils {
	private static Logger LOGGER = Logger.of(SMSUtils.class);
	private static String ApiKey;

	public static void intialize(String apiKey) {
		ApiKey = apiKey;
	}

	@Nullable
	public static String sendMessage(String message, String sender, String number) {
		try {
			String urlString = String.format("https://api.txtlocal.com/send/?%s&%s&%s&%s",
					"apikey=" + URLEncoder.encode(ApiKey, "UTF-8"), "message=" + URLEncoder.encode(message, "UTF-8"),
					"sender=" + URLEncoder.encode(sender, "UTF-8"), "numbers=" + URLEncoder.encode(number, "UTF-8"));

			try {
				URL url = new URL(urlString);
				return HttpRequestSender.sendRequest(url);
			} catch (Exception e) {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Wait what. This shouldn't happen. The encoding type is hard-coded to be set to UTF-8");
			return null;
		}
	}
}