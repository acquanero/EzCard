package com.acquanero.ezcard.io;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://localhost:3000/api/";

    public static EzCardApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(EzCardApiService.class);
    }
}
