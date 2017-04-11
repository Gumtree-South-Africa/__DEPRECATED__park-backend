package com.ebay.park.eps;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.ProxySelector;

/**
 * @author l.marino on 7/2/15.
 */
@Component
public class EPSClient {

    @Value("${eps.url}")
    private String url;

    @Value("${eps.token}")
    private String token;

    @Value("${eps.upload_days}")
    private String uploadDays;

    @Value("${eps.extension_days}")
    private String extensionDays;

    @Value("${eps.api_version}")
    private String apiVersion;

    @Value("${eps.site_id}")
    private String siteId;

    @Value("${eps.dev_id}")
    private String devId;

    @Value("${eps.app_id}")
    private String appId;

    @Value("${eps.cert_id}")
    private String certId;

    @Value("${eps.verb}")
    private String verb;

    @Value("${eps.verb_extend}")
    private String verbExtend;

    @Value("${eps.ack_type_accepted}")
    private String ackTypeAccepted;

    @Value("${eps.conn_timeout}")
    private int timeOut;

    private static Logger logger = LoggerFactory.getLogger(EPSClient.class);

    /**
     * Post an image to EPS from a Picture File.
     *
     * @param name
     * @param file
     * @return url
     */
    public String publish(String name, MultipartFile file) {
        try {
            return publish(name, file.getBytes());
        } catch (IOException e) {
            logger.error(
                    "An exception occurred when trying to open the picture before posting to EPS",
                    e);
        }
        return null;
    }

    /**
     * Post an image to EPS from a byte array.
     *
     * @param name the picture name
     * @param file the file in a byte array
     * @return url
     */
    public String publish(String name, byte[] file) {
        HttpPost postMethod = buildPost(name, "", "", file, true);
        return postToEPS(postMethod);
    }

    /**
     * Post an image to EPS from a url.
     *
     * @param name
     * @param externalUrl
     * @return
     */
    public String publishFromUrl(String name, String externalUrl) {
        HttpPost postMethod = buildPost(name, "", externalUrl, null, true);
        return postToEPS(postMethod);
    }

    /**
     * Refresh picture's expire time into EPS.
     *
     * @param picUrl the EPS image's url to update
     * @return picUrl
     */
    public String update(String picUrl) {
        HttpPost postMethod = buildPost("", picUrl, "", null, false);
        return postToEPS(postMethod);
    }

    private String postToEPS(HttpPost request){
        String pictureUrl = "";
        CloseableHttpResponse response = null;

        try {
            //Post Image
            response = getHttpClient().execute(request);
            logger.debug("Post Image response: {}", response.toString());

            HttpEntity entity = response.getEntity();
            
            String message = EntityUtils.toString(entity);
            logger.debug("Post Image response: {}", message);

            //Retrieve image url
            pictureUrl = handleResponse(message);

            //Consume & close stream
            EntityUtils.consume(entity);

        } catch (Exception e) {
            logger.error("An exception occurred when trying to connect to the EPS", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("An exception occurred when trying to close the connection to the EPS", e);
            }
        }
        return pictureUrl;
    }


    /**
     * Creates an instance of {@link CloseableHttpClient} with a system default proxy set up.
     * Note that it takes care of System properties specified in java for using proxies, like <code>http.proxyHost</code> or
     * <code>https.proxyHost</code>
     * @return an httpClient able to execute requests
     * @see <a>https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d5e485</a>
     * @see <a>https://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html</a>
     */
    private CloseableHttpClient getHttpClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeOut * 1000)
                .setConnectionRequestTimeout(timeOut * 1000)
                .setSocketTimeout(timeOut * 1000)
                .build();
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setRoutePlanner(routePlanner)
                .build();
        return httpClient;
    }

    private HttpPost buildPost(String picName, String picUrl, String externalPicUrl, byte[] file, boolean isPost) {
        HttpPost filePost = new HttpPost(url);
        String xml;
        if (isPost) {
            xml = buildXML(picName, picUrl, externalPicUrl, verb, uploadDays, apiVersion, token);
            setHeaders(filePost, apiVersion, devId,
                    appId, certId, verb, siteId);
        } else {
            xml = buildXML(picName, picUrl, externalPicUrl, verbExtend, extensionDays, apiVersion, token);
            setHeaders(filePost, apiVersion, devId,
                    appId, certId, verbExtend, siteId);
        }

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("XML Payload", xml, ContentType.APPLICATION_XML);

        if (file != null) {
                builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, "file.ext");
        }

        HttpEntity entity = builder.build();
        filePost.setEntity(entity);
        return filePost;
    }

    private void setHeaders(HttpPost filePost, String apiVersion, String devId, String appId,
                            String certId, String verb, String siteId) {
        filePost.setHeader("SOAPAction", "");
        filePost.setHeader("X-EBAY-API-COMPATIBILITY-LEVEL", apiVersion);
        filePost.setHeader("X-EBAY-API-DEV-NAME", devId);
        filePost.setHeader("X-EBAY-API-APP-NAME", appId);
        filePost.setHeader("X-EBAY-API-CERT-NAME", certId);
        filePost.setHeader("X-EBAY-API-CALL-NAME", verb);
        filePost.setHeader("X-EBAY-API-SITEID", siteId);
    }

    private String buildXML(String picName, String picURL, String externalUrl, String verb, String days, String apiVersion, String token) {

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("\n");
        xml.append("<" + verb + "Request xmlns=\"urn:ebay:apis:eBLBaseComponents\">");
        xml.append("\n");
        xml.append("<Version>" + apiVersion + "</Version>");
        xml.append("\n");

        if (StringUtils.isNotEmpty(picName)) {
            xml.append("<PictureName>" + picName + "</PictureName>");
            if (!"".equals(externalUrl)) {
                xml.append("\n");
                xml.append("<ExternalPictureURL>" + externalUrl + "</ExternalPictureURL>");
            }
        } else {
            xml.append("<PictureURL>" + picURL + "</PictureURL>");
        }

        xml.append("\n");
        xml.append("<ExtensionInDays>" + days + "</ExtensionInDays>");
        xml.append("\n");
        xml.append("<RequesterCredentials><ebl:eBayAuthToken xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\"><![CDATA[");
        xml.append(token);
        xml.append("]]></ebl:eBayAuthToken></RequesterCredentials>");
        xml.append("\n");
        xml.append("</" + verb + "Request>");
        xml.append("\n");

        return xml.toString();
    }

    private String handleResponse(String outputResponse) throws Exception {

        String picUrl = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new InputSource(new StringReader(outputResponse)));
        document.getDocumentElement().normalize();
        NodeList uploadSiteHostedPicturesResponse = document.getElementsByTagName("UploadSiteHostedPicturesResponse");
        NodeList extendSiteHostedPicturesResponse = document.getElementsByTagName("ExtendSiteHostedPicturesResponse");
        Element ackElement = (Element) document.getElementsByTagName("Ack").item(0);

        if (ackElement.getTextContent() != null
                && !ackTypeAccepted.contains(ackElement.getTextContent())) {
            NodeList errors = document.getElementsByTagName("Errors");

            if (null != errors) {
                Element errorElement = (Element) errors.item(0);
                String errorMessage = errorElement
                        .getElementsByTagName("LongMessage").item(0)
                        .getTextContent();

                logger.error("EPS Error Payload", outputResponse);
                throw new RuntimeException(errorMessage);
            }
        }

        if (uploadSiteHostedPicturesResponse.getLength() > 0) {
            picUrl = document.getElementsByTagName("FullURL").item(0)
                    .getTextContent();

        } else if (extendSiteHostedPicturesResponse.getLength() > 0) {
            picUrl = document.getElementsByTagName("PictureURL").item(0)
                    .getTextContent();
        }

        return picUrl;
    }
}