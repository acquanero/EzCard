package com.acquanero.ezcard.io;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://ezcard.ml:8080/api/";

    public static EzCardApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(EzCardApiService.class);
    }
}
