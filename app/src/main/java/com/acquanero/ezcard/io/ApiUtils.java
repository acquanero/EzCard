package com.acquanero.ezcard.io;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://boiling-hamlet-47835.herokuapp.com/api/";

    public static EzCardApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(EzCardApiService.class);
    }
}
