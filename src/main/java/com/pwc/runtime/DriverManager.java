package com.pwc.runtime;

import com.pwc.core.framework.util.PropertiesUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DriverManager extends Manager {

    private static final Pattern CHROME_WINDOWS_REGEX = Pattern.compile("^.*?chrome.+?win32.*?$");
    private static final Pattern CHROME_LINUX_32_REGEX = Pattern.compile("^.*?chrome.+?linux32.*?$");
    private static final Pattern CHROME_LINUX_64_REGEX = Pattern.compile("^.*?chrome.+?linux64.*?$");
    private static final Pattern CHROME_MAC_FILE_REGEX = Pattern.compile("^.*?chrome.+?mac32.*?$");
    private static final Pattern IE_WINDOWS_32_REGEX = Pattern.compile("^.*?IEDriver.+?Win32.*?$");
    private static final Pattern IE_WINDOWS_64_REGEX = Pattern.compile("^.*?IEDriver.+?x64.*?$");
    private static final Pattern SAFARI_REGEX = Pattern.compile("^.*?safari.*?$");
    private static final Pattern PHANTOM_WINDOWS_REGEX = Pattern.compile("^.*?phantomjs.+?windows.*?$");
    private static final Pattern PHANTOM_MAC_REGEX = Pattern.compile("^.*?phantomjs.+?macosx.*?$");
    private static final Pattern GECKO_WINDOWS_64_REGEX = Pattern.compile("^.*?gecko.+?win64.*?$");

    private static final String CHROME_WINDOWS_FILE_NAME = "chrome/chrome_win.exe";
    private static final String CHROME_LINUX_32_FILE_NAME = "chrome/chrome_linux_32";
    private static final String CHROME_LINUX_64_FILE_NAME = "chrome/chrome_linux_64";
    private static final String CHROME_MAC_FILE_NAME = "chrome/chrome_mac";
    private static final String IE_WINDOWS_32_FILE_NAME = "ie/ie_win32.exe";
    private static final String IE_WINDOWS_64_FILE_NAME = "ie/ie_win64.exe";
    private static final String SAFARI_FILE_NAME = "safari/safaridriver.safariextz";
    private static final String PHANTOMJS_WIN_FILE_NAME = "phantomjs/phantomjs.exe";
    private static final String PHANTOMJS_MAC_FILE_NAME = "phantomjs/phantomjs";
    private static final String GECKO_WINDOWS_64_FILE_NAME = "firefox/geckodriver.exe";
    private static final int BUFFER_SIZE = 1024;

    private static URL chromeUrl;
    private static URL seleniumUrl;
    private static URL phantomJsUrl;
    private static URL geckoUrl;

    /**
     * Entry point to Driver Manager application used to download and un-package/unzip drivers accordingly
     *
     * @param keys program arguments of filename of driver artifact to download.
     *             For example "v0.11.1/geckodriver-v0.11.1-win64.zip"
     */
    public static void main(String[] keys) {

        try {

            Properties properties = PropertiesUtils.getPropertiesFromPropertyFile("driver.properties");
            URL baseUrl = DriverManager.class.getClassLoader().getResource("drivers/");

            chromeUrl = new URL((String) properties.get("chrome.driver.url"));
            seleniumUrl = new URL((String) properties.get("selenium.release.driver.url"));
            phantomJsUrl = new URL((String) properties.get("phantomjs.driver.url"));
            geckoUrl = new URL((String) properties.get("gecko.driver.url"));

            for (String key : keys) {

                URL targetUrl = getChromeUrl();
                String targetFileName = null;
                if (CHROME_WINDOWS_REGEX.matcher(key).find()) {
                    targetFileName = CHROME_WINDOWS_FILE_NAME;
                } else if (CHROME_LINUX_32_REGEX.matcher(key).find()) {
                    targetFileName = CHROME_LINUX_32_FILE_NAME;
                } else if (CHROME_LINUX_64_REGEX.matcher(key).find()) {
                    targetFileName = CHROME_LINUX_64_FILE_NAME;
                } else if (CHROME_MAC_FILE_REGEX.matcher(key).find()) {
                    targetFileName = CHROME_MAC_FILE_NAME;
                } else if (IE_WINDOWS_32_REGEX.matcher(key).find()) {
                    targetFileName = IE_WINDOWS_32_FILE_NAME;
                    targetUrl = getSeleniumUrl();
                } else if (IE_WINDOWS_64_REGEX.matcher(key).find()) {
                    targetFileName = IE_WINDOWS_64_FILE_NAME;
                    targetUrl = getSeleniumUrl();
                } else if (SAFARI_REGEX.matcher(key).find()) {
                    targetFileName = SAFARI_FILE_NAME;
                    targetUrl = getSeleniumUrl();
                } else if (PHANTOM_WINDOWS_REGEX.matcher(key).find()) {
                    targetFileName = PHANTOMJS_WIN_FILE_NAME;
                    targetUrl = getPhantomJsUrl();
                } else if (PHANTOM_MAC_REGEX.matcher(key).find()) {
                    targetFileName = PHANTOMJS_MAC_FILE_NAME;
                    targetUrl = getPhantomJsUrl();
                } else if (GECKO_WINDOWS_64_REGEX.matcher(key).find()) {
                    targetFileName = GECKO_WINDOWS_64_FILE_NAME;
                    targetUrl = getGeckoUrl();
                }

                File targetFile = new File(baseUrl.getPath() + File.separator + targetFileName);
                boolean success = downloadDriver(targetFile, targetUrl, key);
                if (success) {
                    System.out.println(String.format("Successfully Downloaded driver='%s'", key));
                } else {
                    System.out.println(String.format("Failed to Download driver='%s'", key));
                }

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * Download the driver(s) from the web
     *
     * @param targetFile complete path to target file to download
     * @param driverUrl  target file URL location on the web
     * @param key        exact filename of driver artifact to download
     * @return success/fail flag of a successful download
     */
    protected static boolean downloadDriver(File targetFile, URL driverUrl, String key) {

        StringBuilder url = new StringBuilder();
        HashMap contentRecord = getDriverContentByKey(driverUrl, key);
        url.append(driverUrl.toString());

        if (contentRecord != null) {
            url.append(contentRecord.get("Key"));
        } else {
            url.append(key);
        }

        try {
            if (StringUtils.containsIgnoreCase(url.toString(), "zip")) {

                File unzippedTempFile = getFileFromArchive(url.toString());

                byte[] buffer = new byte[BUFFER_SIZE];
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(unzippedTempFile));
                ZipEntry zipEntry = zipInputStream.getNextEntry();

                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                    Thread.sleep(3000);
                    targetFile.createNewFile();
                    targetFile.setWritable(true);
                }

                System.out.println("Unzipping and creating file : " + targetFile.getAbsoluteFile());
                while (zipEntry != null) {

                    FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.close();
                    zipEntry = zipInputStream.getNextEntry();
                }

                zipInputStream.closeEntry();
                zipInputStream.close();

                if (unzippedTempFile.exists()) {
                    unzippedTempFile.delete();
                }

            } else {
                FileUtils.copyURLToFile(new URL(url.toString()), targetFile);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static URL getChromeUrl() {
        return chromeUrl;
    }

    public void setChromeUrl(URL chromeUrl) {
        this.chromeUrl = chromeUrl;
    }

    public static URL getSeleniumUrl() {
        return seleniumUrl;
    }

    public void setIeUrl(URL ieUrl) {
        this.seleniumUrl = ieUrl;
    }

    public static URL getPhantomJsUrl() {
        return phantomJsUrl;
    }

    public static URL getGeckoUrl() {
        return geckoUrl;
    }

    public static void setPhantomJsUrl(URL phantomJsUrl) {
        DriverManager.phantomJsUrl = phantomJsUrl;
    }

}