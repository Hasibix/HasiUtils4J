package io.hasibix.hasiutils;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TwoFAUtils {
	private static final Logger LOGGER = Logger.of(TwoFAUtils.class);

	public static String[] pair(URL url, String appName, String appInfo, String secretCode) {
		try {
			String urlString = String.format("%s?%s&%s&%s", url.toString(),
					"appName=" + URLEncoder.encode(appName, "UTF-8"), "appInfo=" + URLEncoder.encode(appInfo, "UTF-8"),
					"secretCode=" + URLEncoder.encode(secretCode, "UTF-8"));

			try {
				String response = HttpRequestSender.sendRequest(new URL(urlString));

				if (response != null) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new InputSource(new StringReader(response)));

					Element root = doc.getDocumentElement();

					NodeList manualSetupCodeList = root.getElementsByTagName("ManualSetupCode");
					Element manualSetupCodeElement = (Element) manualSetupCodeList.item(0);
					String manualSetupCode = manualSetupCodeElement.getTextContent();

					NodeList htmlList = root.getElementsByTagName("Html");
					Element htmlElement = (Element) htmlList.item(0);
					String html = htmlElement.getTextContent();

					String qrCodeImageUrl = html.substring(html.indexOf("src='") + "src='".length(),
							html.indexOf("' border=0"));

					return new String[] { manualSetupCode, qrCodeImageUrl };
				} else {
					LOGGER.error("An error occurred while sending an HTTP request.");
					return null;
				}
			} catch (Exception e) {
				LOGGER.error("An exception occurred while trying to create a pairing request!");
				LOGGER.trace(e);
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Wait what. This shouldn't happen. The encoding type is hard-coded to be set to UTF-8");
			return null;
		}
	}

	public static boolean validate(URL url, String pin, String secretCode) {
		try {
			String urlString = String.format("%s?%s&%s&%s", url.toString(), "pin=" + URLEncoder.encode(pin, "UTF-8"),
					"secretCode=" + URLEncoder.encode(secretCode, "UTF-8"));

			try {
				String response = HttpRequestSender.sendRequest(new URL(urlString));

				if (response != null) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new InputSource(new StringReader(response)));

					Element root = doc.getDocumentElement();

					NodeList isValidList = root.getElementsByTagName("boolean");
					Element isValidElement = (Element) isValidList.item(0);
					boolean isValid = Boolean.parseBoolean(isValidElement.getTextContent());

					return isValid;
				} else {
					LOGGER.error("An error occurred while sending an HTTP request.");
					return false;
				}
			} catch (Exception e) {
				LOGGER.error("An exception occurred while trying to create a validation check request!");
				LOGGER.trace(e);
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Wait what. This shouldn't happen. The encoding type is hard-coded to be set to UTF-8");
			return false;
		}
	}
}