package com.pwc.runtime;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ManagerTest {

    private String baseKey;
    private URL mockGoogleAPIUrl;
    private URL mockBitBucketAPIUrl;
    private URL mockGeckoAPIUrl;

    @Before
    public void setUp() {

        try {

            baseKey = "2.21/chromedriver_win32.zip";
            mockGoogleAPIUrl = new URL("http://chromedriver.storage.googleapis.com/");
            mockBitBucketAPIUrl = new URL("https://bitbucket.org/ariya/phantomjs/downloads/");
            mockGeckoAPIUrl = new URL("https://github.com/mozilla/geckodriver/releases/download/");

            HashMap mockContentMap = mock(HashMap.class);
            when(mockContentMap.get("Owner")).thenReturn("");
            when(mockContentMap.get("ETag")).thenReturn("8a93dc3ff02ef9bc3161dd4b20f87215");
            when(mockContentMap.get("Key")).thenReturn("2.21/chromedriver_win32.zip");
            when(mockContentMap.get("MetaGeneration")).thenReturn("1");
            when(mockContentMap.get("Generation")).thenReturn("1453790823425000");
            when(mockContentMap.get("LastModified")).thenReturn("2016-01-26T06:47:03.424Z");
            when(mockContentMap.get("Size")).thenReturn("2598298");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getFileFromZipArchiveTest() {
        File result = Manager.getFileFromArchive(mockGoogleAPIUrl + baseKey);
        assertEquals("temp", result.getName());
    }

    @Test
    public void getDriverContentByKeyGoogleAPITest() {
        HashMap result = Manager.getDriverContentByKey(mockGoogleAPIUrl, baseKey);
        HashMap spyMap = spy(result);
        assertEquals(spyMap.size(), 7);
    }

    @Test
    public void getDriverContentByKeyGeckoAPITest() {
        HashMap result = Manager.getDriverContentByKey(mockGeckoAPIUrl, baseKey);
        assertNull(result);
    }

    @Test
    public void getDriverContentByKeyBitBucketTest() {
        HashMap result = Manager.getDriverContentByKey(mockBitBucketAPIUrl, baseKey);
        assertNull(result);
    }

    @Test
    public void getDriverXmlAsJsonGoogleApiTest() {
        JsonPath jsonPath;
        jsonPath = Manager.getDriverXmlAsJson(mockGoogleAPIUrl);
        JsonPath spyJsonPath = spy(jsonPath);
        HashMap resultMap = spyJsonPath.get();
        HashMap listBucketResultMap = (HashMap) resultMap.get("ListBucketResult");
        assertEquals(listBucketResultMap.size(), 6);
    }

    @Test(expected = NullPointerException.class)
    public void getDriverXmlAsJsonOtherApiTest() {
        JsonPath jsonPath;
        jsonPath = Manager.getDriverXmlAsJson(mockBitBucketAPIUrl);
        spy(jsonPath);
    }

}
