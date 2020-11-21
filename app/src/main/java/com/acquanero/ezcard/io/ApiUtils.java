package com.acquanero.ezcard.io;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://limitless-dusk-17177.herokuapp.com/api/";

    public static EzCardApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(EzCardApiService.class);
    }
}
