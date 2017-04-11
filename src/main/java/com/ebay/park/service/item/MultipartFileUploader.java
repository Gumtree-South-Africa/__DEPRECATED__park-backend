package com.ebay.park.service.item;
 
import java.io.File;
import java.io.IOException;
import java.util.List;
 
/**
 * This program demonstrates a usage of the MultipartUtility class.
 * @author www.codejava.net
 *
 */
public class MultipartFileUploader {
 
    public static void main(String[] args) {
        String charset = "UTF-8";
        File uploadFile1 = new File("c:/x1.JPG");
        File uploadFile2 = new File("c:/x2.JPG");
        String requestURL = "http://localhost:8080/item/photo/upload";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
             
            multipart.addFormField("photo1Url", "");
            multipart.addFormField("photo2Url", "");
            multipart.addFormField("photo3Url", "url3");
            multipart.addFormField("photo4Url", "url4");
             
            //multipart.addFilePart("fileUpload", uploadFile1);
            //multipart.addFilePart("fileUpload", uploadFile2);
            multipart.addFilePart("photo1", uploadFile1);
            multipart.addFilePart("photo2", uploadFile2);
 
            List<String> response = multipart.finish();
             
            System.out.println("SERVER REPLIED:");
             
            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}