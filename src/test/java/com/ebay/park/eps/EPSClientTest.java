package com.ebay.park.eps;

import com.ebay.park.config.MessagesConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author l.marino on 7/9/15.
 */
@ContextConfiguration(classes = {MessagesConfig.class})
public class EPSClientTest {

    @InjectMocks
    private EPSClient client = new EPSClient();

    MultipartFile fileMock = mock(MultipartFile.class);
    String uploadResponseXml;
    String extendedUploadResponse;
    String failureResponse;
    String url = "https://api.sandbox.ebay.com/ws/api.dll";
    String token = "AgAAAA**AQAAAA**aAAAAA**yeFbVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4GhCJGLpAydj6x9nY+seQ**X60BAA**AAMAAA**N7T4wsT9Ju+MfgG2V/6QpTHVbPJ9PDG6nNDMyS0hoWLI+4mi4xCKmSmhHvqn1tomMK2On4AHgsg6tk8zB0kUlUtaMEvySDyNJDi9IFw7tsOiy+Jmh9yrZXSiQ0DvYbr37XRGT3upJo7RcpKW9tUDiIkrpI4N6oZNV0UaL8DZxMXQFQIooedrao0vtDmLMZkaqhl9/EhpmnfSszT2UA5LkYeSihU2x6j1Wg45+iRNVV0+oFoPgI3nK50WxIyBBVd2rWaBW0rm76rEdL6J9MFwVhQ3/HPfSjeTjtytzzUIcVyBUKF9jk1gj/1JXdXhy23iXWO2XbWR9SJg7xedyDXktpp5e+JZIjTUmvEH9DTW9gmONwmDzAD7t40xuCAc5b56I35aa5kM7s+sUR7TjvsGkmG9l8w1MDZfko8RxTXDiUN1Kg0zUp2lmvHHTkv0keSmxexs8JTY9pjLVd8m0e3xURAqfIMGdYJnj+mcJek5ZQZUgOgPG6Mor+j7XHqf2SIW6mOlvHOL3F4nST2QEDRdgnCAEeS3G5V/CiiQizOX2TWBJ3q6huo5EaN5x8RR6giCMWfbdrnbfPDz6lboR680sBZVgOqSoI5HcjohTXB2QsfD7kUqy3GImaK4SchEZ7JHyaAw51wkp8pMVmtN5hZhgzbiyQkefkpVYiA64Bz0MwQuK6QgIXhm5JW+zUY84mXOrXNrAP2fDglT8SCc7pm1zNj2E61OT17b9+Mm8DU3WLcaAAkMKG1FnkGZZcmD0Lfn";
    String uploadDays = "10";
    String extensionDays = "90";
    String apiVersion = "517";
    String site_id = "0";
    String dev_id = "54ff67d1-ce84-4e8e-b3ea-8e3586c2e3f6";
    String app_id = "eBayClas-6322-436c-9865-3de70df7a632";
    String cert_id = "6e2ec48d-a4c0-48a2-967c-42d79a8aa26c";
    String verb = "UploadSiteHostedPictures";
    String verbExtend = " ExtendSiteHostedPictures";
    String conn_timeout = "30";
    String picSize500 = "500x500";
    String picSize300 = "300x300";
    String picName = "imgDummyName";
    String picUrl = "http://i.ebayimg.sandbox.ebay.com/00/s/NjAwWDYwMA==/z/V3MAAOSw6XdVnEu5/$_12.JPG";
    String externalPicUrl = "http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/park031915/item/1/2015-05-04T19:57:53+0000_1430769473150";
    String picUrlSize500 = "http://i.ebayimg.sandbox.ebay.com/00/s/NjAwWDYwMA==/z/V3MAAOSw6XdVnEu5/$_12.JPG";
    String picUrlSize300 = "http://i.ebayimg.sandbox.ebay.com/00/s/NjAwWDYwMA==/z/V3MAAOSw6XdVnEu5/$_35.JPG";
    String failureMessage = "Invalid Picture URL https://api.sandbox.ebay.com/ws/api.dll. Please provide a valid picture url";
    String expectedXmlForUpload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<" + verb + "Request xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n" +
            "<Version>" + apiVersion + "</Version>\n" +
            "<PictureName>" + picName + "</PictureName>\n" +
            "<ExtensionInDays>" + uploadDays + "</ExtensionInDays>\n" +
            "<RequesterCredentials><ebl:eBayAuthToken xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\"><![CDATA[" +
            token +
            "]]></ebl:eBayAuthToken></RequesterCredentials>\n" +
            "</" + verb + "Request>\n";

    String expectedXmlForUploadFromURL = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<" + verb + "Request xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n" +
            "<Version>" + apiVersion + "</Version>\n" +
            "<PictureName>" + picName + "</PictureName>\n" +
            "<ExternalPictureURL>" + externalPicUrl + "</ExternalPictureURL>\n" +
            "<ExtensionInDays>" + uploadDays + "</ExtensionInDays>\n" +
            "<RequesterCredentials><ebl:eBayAuthToken xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\"><![CDATA[" +
            token +
            "]]></ebl:eBayAuthToken></RequesterCredentials>\n" +
            "</" + verb + "Request>\n";

    String expectedXmlForExUpload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<" + verbExtend + "Request xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n" +
            "<Version>" + apiVersion + "</Version>\n" +
            "<PictureURL>" + picUrl + "</PictureURL>\n" +
            "<ExtensionInDays>" + extensionDays + "</ExtensionInDays>\n" +
            "<RequesterCredentials><ebl:eBayAuthToken xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\"><![CDATA[" +
            token +
            "]]></ebl:eBayAuthToken></RequesterCredentials>\n" +
            "</" + verbExtend + "Request>\n";


    @Before
    public void setup() throws IOException {
      /*  initMocks(this);
        when(fileMock.getName()).thenReturn("filename");
        when(fileMock.getSize()).thenReturn(10l);
        when(fileMock.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
        client.ackTypeAccepted = "Success,Warning";
        uploadResponseXml = IOUtils.toString(
                this.getClass().getResourceAsStream("UploadSiteHostedPicturesResponse.xml"),
                "UTF-8"
        );
        extendedUploadResponse = IOUtils.toString(
                this.getClass().getResourceAsStream("ExtendSiteHostedPicturesResponse.xml"),
                "UTF-8"
        );
        failureResponse = IOUtils.toString(
                this.getClass().getResourceAsStream("ExtendSiteHostedPicturesResponseFailure.xml"),
                "UTF-8"
        );*/

    }

    @Test
    public void buildXmlForUpload() {
        //String requestXml = client.buildXML(picName, "", "", verb, uploadDays, apiVersion, token);
        // assertEquals(requestXml, expectedXmlForUpload);
    }

    @Test
    public void buildXmlForUploadUrl() {
        //String requestXml = client.buildXML(picName, "", externalPicUrl, verb, uploadDays, apiVersion, token);
        //assertEquals(requestXml, expectedXmlForUploadFromURL);
    }

    @Test
    public void buildXmlForExUpload() {
        //String requestXml = client.buildXML("", picUrl, "", verbExtend, extensionDays, apiVersion, token);
        // assertEquals(requestXml, expectedXmlForExUpload);
    }

    @Test
    public void handleResponseForExUpload() {
    }

    @Test
    public void handleResponseForFailure() {

        try {
            // client.handleResponse(failureResponse, 200);
        } catch (Exception e) {
            assertEquals(e.getMessage(), failureMessage);
        }
    }

    @Test
    public void getFormParts(){
        // assertEquals(client.getFormParts(expectedXmlForUpload,null).length,1);
        //assertEquals(client.getFormParts(expectedXmlForUpload, fileMock).length,2);
    }
}
