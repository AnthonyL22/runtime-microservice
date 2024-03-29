package com.pwc.runtime;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DriverManagerTest {

    private static final String SELENIUM_SERVER_FILE_NAME = "selenium-server-standalone.jar";
    private static final String CHROME_WINDOWS_FILE_NAME = "chrome/chrome_win.exe";
    private static final String CHROME_LINUX_64_FILE_NAME = "chrome/chrome_linux_64";
    private static final String CHROME_MAC_FILE_NAME = "chrome/chrome_mac";
    private static final String IE_WINDOWS_32_FILE_NAME = "ie/ie_win32.exe";
    private static final String IE_WINDOWS_64_FILE_NAME = "ie/ie_win64.exe";
    private static final String SAFARI_FILE_NAME = "safari/safaridriver.safariextz";
    private static final String FIREFOX_WINDOWS_FILE_NAME = "firefox/geckodriver_mac.exe";
    private static final String GECKO_MAC_FILE_NAME = "firefox/geckodriver_mac";
    private static final String GECKO_LINUX_64_FILE_NAME = "firefox/geckodriver_linux_64";
    private static final String EDGE_WINDOWS_FILE_NAME = "edge/edge_win.exe";
    private static final String EDGE_MAC_FILE_NAME = "edge/edge_mac";

    private static final String SELENIUM_SERVER_JAR_FILE_NAME = "3.4/selenium-server-standalone-3.4.0.jar";
    private static final String CHROME_WINDOWS_ZIP_FILE_NAME = "115.0.5790.98/win64/chromedriver-win64.zip";
    private static final String CHROME_LINUX_64_ARCHIVE_FILE_NAME = "115.0.5790.98/linux64/chromedriver-linux64.zip";
    private static final String CHROME_MAC_ARCHIVE_FILE_NAME = "115.0.5790.98/mac-x64/chromedriver-mac-x64.zip";
    private static final String CHROME_IE_32_ARCHIVE_FILE_NAME = "2.53/IEDriverServer_Win32_2.53.1.zip";
    private static final String CHROME_IE_64_ARCHIVE_FILE_NAME = "2.53/IEDriverServer_x64_2.53.1.zip";
    private static final String SAFARI_ARCHIVE_FILE_NAME = "2.48/SafariDriver.safariextz";
    private static final String FIREFOX_ARCHIVE_FILE_NAME = "v0.31.0/geckodriver-v0.31.0-win64.zip";
    private static final String FIREFOX_ARCHIVE_MAC_FILE_NAME = "v0.24.0/geckodriver_mac-v0.24.0-macos.tar.gz";
    private static final String FIREFOX_ARCHIVE_LINUX_FILE_NAME = "v0.24.0/geckodriver_mac-v0.24.0-linux64.tar.gz";
    private static final String EDGE_WINDOWS_ARCHIVE_FILE_NAME = "110.0.1587.50/edgedriver_win64.zip";
    private static final String EDGE_MAC_ARCHIVE_FILE_NAME = "110.0.1587.50/edgedriver_mac64.zip";

    private static final String CHROME_DRIVER_URL = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/";
    private static final String FIREFOX_DRIVER_URL = "https://github.com/mozilla/geckodriver/releases/download/";

    private String baseNonZipTargetFileName;
    private String baseKeyNonZipFile;
    private URL mockGoogleAPIUrl;
    private URL mockGeckoAPIUrl;

    @Before
    public void setUp() {

        try {

            baseNonZipTargetFileName = "chrome/chrome_win.bz2";
            baseKeyNonZipFile = "v0.30.0/geckodriver_mac-v0.30.0-macos.tar.gz";
            mockGoogleAPIUrl = new URL(CHROME_DRIVER_URL);
            mockGeckoAPIUrl = new URL(FIREFOX_DRIVER_URL);

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
    public void downloadServerJarTargetFileNotExistTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("grid/");
        File targetFile = new File(baseUrl.getPath() + File.separator + SELENIUM_SERVER_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        boolean result = DriverManager.downloadDriver(targetFile, mockGoogleAPIUrl, SELENIUM_SERVER_JAR_FILE_NAME);
        assertFalse(result);
    }

    @Test
    public void downloadDriverZipTargetFileNotExistTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + CHROME_WINDOWS_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        boolean result = DriverManager.downloadDriver(targetFile, mockGoogleAPIUrl, CHROME_WINDOWS_ZIP_FILE_NAME);
        assertTrue(result);
    }

    @Test
    public void downloadDriverZipTargetFileDoesExistTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + CHROME_WINDOWS_FILE_NAME);
        boolean result = DriverManager.downloadDriver(targetFile, mockGoogleAPIUrl, CHROME_WINDOWS_ZIP_FILE_NAME);
        assertTrue("Create the Target File initially", result);
        result = DriverManager.downloadDriver(targetFile, mockGoogleAPIUrl, CHROME_WINDOWS_ZIP_FILE_NAME);
        assertTrue("Test that a new Target File is not created once existing", result);
    }

    @Test
    public void downloadDriverNonZipTargetFileTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + baseNonZipTargetFileName);
        boolean result = DriverManager.downloadDriver(targetFile, mockGoogleAPIUrl, baseKeyNonZipFile);
        assertTrue(result);
    }

    @Test
    public void mainSuccessfulDownloadSeleniumServerTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("grid/");
        File targetFile = new File(baseUrl.getPath() + File.separator + SELENIUM_SERVER_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {SELENIUM_SERVER_JAR_FILE_NAME});
        assertTrue("Selenium Server File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(SELENIUM_SERVER_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadChromeTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + CHROME_WINDOWS_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {CHROME_WINDOWS_ZIP_FILE_NAME});
        assertTrue("Chrome Win File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(CHROME_WINDOWS_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadLinux64Test() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + CHROME_LINUX_64_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {CHROME_LINUX_64_ARCHIVE_FILE_NAME});
        assertTrue("Chrome Linux64 File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(CHROME_LINUX_64_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadMacTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + CHROME_MAC_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {CHROME_MAC_ARCHIVE_FILE_NAME});
        assertTrue("Chrome Mac File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(CHROME_MAC_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadIEWin32Test() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + IE_WINDOWS_32_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {CHROME_IE_32_ARCHIVE_FILE_NAME});
        assertTrue("IE Win32 File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(IE_WINDOWS_32_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadIEWin64Test() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + IE_WINDOWS_64_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {CHROME_IE_64_ARCHIVE_FILE_NAME});
        assertTrue("IE Win64 File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(IE_WINDOWS_64_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadSafariTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + SAFARI_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {SAFARI_ARCHIVE_FILE_NAME});
        assertTrue("Safari File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(SAFARI_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadWindowsGeckoTest() {
        DriverManager.main(new String[] {FIREFOX_ARCHIVE_FILE_NAME});
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + FIREFOX_WINDOWS_FILE_NAME);
        assertFalse("Firefox File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(FIREFOX_WINDOWS_FILE_NAME, "//")));
        if (targetFile.exists()) {
            targetFile.delete();
        }
    }

    @Test
    public void mainSuccessfulDownloadGeckoMacTest() {

        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + GECKO_MAC_FILE_NAME);
        boolean result = DriverManager.downloadDriver(targetFile, mockGeckoAPIUrl, FIREFOX_ARCHIVE_MAC_FILE_NAME);
        assertTrue("Create the Target File initially", result);
    }

    @Test
    public void mainSuccessfulDownloadGeckoLinuxTest() {

        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + GECKO_LINUX_64_FILE_NAME);
        boolean result = DriverManager.downloadDriver(targetFile, mockGeckoAPIUrl, FIREFOX_ARCHIVE_LINUX_FILE_NAME);
        assertTrue("Create the Target File initially", result);
    }

    @Test
    public void mainSuccessfulDownloadEdgeWindowsTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + EDGE_WINDOWS_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {EDGE_WINDOWS_ARCHIVE_FILE_NAME});
        assertTrue("Edge File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(EDGE_WINDOWS_ARCHIVE_FILE_NAME, "//")));
    }

    @Test
    public void mainSuccessfulDownloadEdgeMacTest() {
        URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");
        File targetFile = new File(baseUrl.getPath() + File.separator + EDGE_MAC_FILE_NAME);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DriverManager.main(new String[] {EDGE_MAC_ARCHIVE_FILE_NAME});
        assertTrue("Edge File was downloaded successfully", targetFile.exists());
        assertTrue("File name matches what was downloaded", targetFile.getName().contains(StringUtils.substringAfterLast(EDGE_WINDOWS_ARCHIVE_FILE_NAME, "//")));
    }

    @Test
    public void mainFailedDownloadTest() {
        DriverManager.main(new String[] {CHROME_WINDOWS_ZIP_FILE_NAME});
    }

}
