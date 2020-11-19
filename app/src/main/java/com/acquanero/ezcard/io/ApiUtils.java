package com.acquanero.ezcard.io;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://186.136.87.160:8080/api/";

    public static EzCardApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(EzCardApiService.class);
    }
}
