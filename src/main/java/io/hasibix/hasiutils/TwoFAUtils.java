package io.hasibix.hasiutils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;

public class TwoFAUtils {
    private static final Logger logger = Logger.of(TwoFAUtils.class);

    public static String[] Pair(HttpTransport client, String appName, String appInfo, String secretCode) {
        HttpRequest request;

        try {
            request = client.createRequestFactory().buildGetRequest(
                new GenericUrl(
                    String.format("%s?%s&%s&%s",
                        "https://authenticatorapi.com/api.asmx/Pair",
                        "appName=" + new UrlEncodedContent(appName).toString(),
                        "appInfo=" + new UrlEncodedContent(appInfo).toString(),
                        "secretCode=" + new UrlEncodedContent(secretCode).toString() 
                    )
                )
            );
        } catch (IOException e) {
            logger.error("An exception occured while trying to create a pairing request!");
            logger.trace(e);
            request = null;
        }

        HttpResponse response;

        if(request != null) {
            response = HttpRequestSender.SendAsync(request);
        } else {
            response = null;
        }

        if(response != null) {
            try {
                String responseData = response.parseAsString();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(responseData)));

                Element root = doc.getDocumentElement();

                NodeList manualSetupCodeList = root.getElementsByTagName("ManualSetupCode");
                Element manualSetupCodeElement = (Element) manualSetupCodeList.item(0);
                String manualSetupCode = manualSetupCodeElement.getTextContent();

                NodeList htmlList = root.getElementsByTagName("Html");
                Element htmlElement = (Element) htmlList.item(0);
                String html = htmlElement.getTextContent();

                String qrCodeImageUrl = html.substring(html.indexOf("src='") + "src='".length(), html.indexOf("' border=0"));

                List<String> result = new ArrayList<String>(2);
                result.add(manualSetupCode);
                result.add(qrCodeImageUrl);

                return result.toArray(new String[2]);
            } catch (Exception e) {
                logger.error("An exception occured while trying to create a pairing request!");
                logger.trace(e);
            }
        }
        return null;
    }

    public static boolean Validate(HttpTransport client, String pin, String secretCode) {
        HttpRequest request;

        try {
            request = client.createRequestFactory().buildGetRequest(
                new GenericUrl(
                    String.format("%s?%s&%s&%s",
                        "https://authenticatorapi.com/api.asmx/ValidatePin",
                        "pin=" + new UrlEncodedContent(pin).toString(),
                        "secretCode=" + new UrlEncodedContent(secretCode).toString()
                    )
                )
            );
        } catch (IOException e) {
            logger.error("An exception occured while trying to create a validation check request!");
            logger.trace(e);
            request = null;
        }

        HttpResponse response;

        if(request != null) {
            response = HttpRequestSender.SendAsync(request);
        } else {
            response = null;
        }

        if(response != null) {
            try {
                String responseData = response.parseAsString();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(responseData)));

                Element root = doc.getDocumentElement();

                NodeList isValidList = root.getElementsByTagName("boolean");
                Element isValidElement = (Element) isValidList.item(0);
                boolean isValid = Boolean.getBoolean(isValidElement.getTextContent());

                return isValid;
            } catch (Exception e) {
                logger.error("An exception occured while trying to create a validation check request!");
                logger.trace(e);
            }
        }
        return false;
    }
}