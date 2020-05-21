package com.pwc.runtime;

import com.jayway.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class Manager {

    /**
     * Convert XML to JSON from driver version APIs.
     *
     * @param url driver URL
     * @return JSON representation of XML from WEB api
     */
    protected static JsonPath getDriverXmlAsJson(final URL url) {

        JsonPath jsonObject = null;
        if (StringUtils.containsIgnoreCase(url.toString(), "googleapis")) {

            try {

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(url.openStream());

                DOMSource domSource = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result);

                JSONObject json = XML.toJSONObject(writer.toString());
                jsonObject = new JsonPath(json.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    /**
     * Get driver JSON Content for a given version.
     *
     * @param url       driver URL
     * @param targetKey filename of driver artifact (zip, rar, etc...)  to download
     * @return map of driver contents
     */
    protected static HashMap getDriverContentByKey(final URL url, String targetKey) {

        if (!url.getHost().contains("api")) {
            return null;
        }

        HashMap foundContent = new HashMap();
        JsonPath jsonObject = getDriverXmlAsJson(url);
        if (jsonObject == null) {
            String htmlWebsiteAPIToJSON = String.format(
                            "{\"ListBucketResult\":{\"Contents\":[{\"Owner\":{},\"Key\":\"%s\",}],\"xmlns\":\"http: //doc.s3.amazonaws.com/2006-03-01\",\"Prefix\":{},\"Marker\":{}}}", targetKey);
            jsonObject = new JsonPath(htmlWebsiteAPIToJSON);
        }
        HashMap results = jsonObject.get("ListBucketResult");

        List<HashMap> contents = (List) results.get("Contents");
        for (HashMap content : contents) {
            if (StringUtils.equalsIgnoreCase(content.get("Key").toString(), targetKey)) {
                foundContent = content;
            }
        }
        return foundContent;
    }

    /**
     * Unzip a zip archive and return single file of it's contents.
     *
     * @param filePath path to ZIP file
     * @return full file that was inside ZIP archive
     */
    protected static File getFileFromArchive(String filePath) {

        URL tempUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File tempFile = new File(tempUrl.getPath() + File.separator + "temp");
        if (tempFile.exists()) {
            tempFile.delete();
        }

        try {
            URL url = new URL(filePath);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(tempFile);
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
            }
            in.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempFile.setWritable(true);
        tempFile.setExecutable(true);
        tempFile.setReadable(true);
        return tempFile;
    }

    /**
     * Download GZip file from web address.
     *
     * @param filePath path to ZIP file
     * @return full file that was inside ZIP archive
     */
    protected static File getFileFromGZip(String filePath) {

        URL tempUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File tempFile = new File(tempUrl.getPath() + File.separator + StringUtils.substringAfterLast(filePath, "/"));
        if (tempFile.exists()) {
            tempFile.delete();
        }

        try {
            URL url = new URL(filePath);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(tempFile);
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
            }
            in.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempFile.setWritable(true);
        tempFile.setExecutable(true);
        tempFile.setReadable(true);
        return tempFile;
    }

}
