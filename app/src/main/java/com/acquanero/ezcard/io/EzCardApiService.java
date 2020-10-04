package com.acquanero.ezcard.io;

import com.acquanero.ezcard.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

//interface que define las rutas a la API

public interface EzCardApiService {

    @FormUrlEncoded
    @POST("user/login/")
    Call<UserInfo> getUserInfo(
            @Field("mail") String mail,
            @Field("password") String password
    );
}
