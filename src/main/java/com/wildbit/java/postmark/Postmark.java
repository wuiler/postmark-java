package com.wildbit.java.postmark;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.wildbit.java.postmark.client.AccountApiClient;
import com.wildbit.java.postmark.client.ApiClient;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Top level class allowing simple access to API clients for doing Postmark API calls.
 */
public class Postmark {

    /**
     * Base Postmark APP Constants
     */
    public enum DEFAULTS {
        API_URL("api.postmarkapp.com"),
        SERVER_AUTH_HEADER("X-Postmark-Server-Token"),
        ACOUNT_AUTH_HEADER("X-Postmark-Account-Token");

        public final String value;

        DEFAULTS(String value) {
            this.value = value;
        }
    }

    /**
     *  Default headers sent always with API requests.
     */
    public static class DefaultHeaders {

        public static MultivaluedMap<String, String> headers() {
            MultivaluedMap<String, String> headerValues = new MultivaluedMapImpl();
            headerValues.add("User-Agent", "Postmark Java Library: " + libraryVersion());
            headerValues.add("Accept", "application/json");
            headerValues.add("Content-Type", "application/json");
            return headerValues;
        }

    }

    public static String libraryVersion()  {
        Properties prop = new Properties();
        InputStream in =  Postmark.class.getClassLoader().getResourceAsStream(".properties");

        try {
            prop.load(in);
        } catch (Exception e) {
            log.warn(e.toString());
        }
        return prop.getProperty("Version");
    }

    // public class methods for accessing API clients

    public static ApiClient getApiClient(String apiToken) {
        return new ApiClient(DEFAULTS.API_URL.value, getHeadersWithAuth(DEFAULTS.SERVER_AUTH_HEADER, apiToken));
    }

    public static ApiClient getApiClient(String apiToken, Boolean secureConnection) {
        return new ApiClient(DEFAULTS.API_URL.value, getHeadersWithAuth(DEFAULTS.SERVER_AUTH_HEADER, apiToken), secureConnection);
    }

    public static AccountApiClient getAccountApiClient(String apiToken) {
        return new AccountApiClient(DEFAULTS.API_URL.value, getHeadersWithAuth(DEFAULTS.ACOUNT_AUTH_HEADER, apiToken));
    }

    public static AccountApiClient getAccountApiClient(String apiToken, Boolean secureConnection) {
        return new AccountApiClient(DEFAULTS.API_URL.value, getHeadersWithAuth(DEFAULTS.ACOUNT_AUTH_HEADER, apiToken), secureConnection);
    }

    // private methods

    private Postmark() {}

    private static Logger log = Logger.getLogger(Postmark.class);

    private static MultivaluedMap getHeadersWithAuth(DEFAULTS authType, String apiToken) {
        MultivaluedMap headers = DefaultHeaders.headers();
        headers.put(authType.value, apiToken);
        return headers;
    }
}
