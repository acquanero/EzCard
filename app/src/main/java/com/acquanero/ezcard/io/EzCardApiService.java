package com.acquanero.ezcard.io;

import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.model.UserIdToken;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//interface que define las rutas a la API

public interface EzCardApiService {

    @FormUrlEncoded
    @POST("user/login/")
    Call<UserIdToken> postDataGetToken(
            @Header("xappid") String xappid,
            @Field("mail") String mail,
            @Field("password") String password

    );

    @GET("user/login/{id}")
    Call<SimpleResponse> logInWithToken(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("id") int id
    );

    @GET("user/{id}")
    public Call<UserData> getUserData(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("user/register/")
    Call<UserIdToken> postToRegister(
            @Header("xappid") String xappid,
            @Field("name") String name,
            @Field("last_name") String last_name,
            @Field("password") String password,
            @Field("email") String mail,
            @Field("cellphone") String phone,
            @Field("pin") int pin
    );

    @FormUrlEncoded
    @POST("user/recoveraccount/")
    Call<SimpleResponse> postToRecover(
            @Header("xappid") String xappid,
            @Field("email") String mail
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "cards/{id}", hasBody = true)
    Call<SimpleResponse> deleteCard(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Path("id") int id,
            @Field("card_id") int cardId
    );

    @FormUrlEncoded
    @PUT("cards/{id}")
    Call<SimpleResponse> putCard(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Path("id") int id,
            @Field("card_id") int cardId,
            @Field("card_name") String cardName,
            @Field("card_icon") int cardIcon
    );

    @FormUrlEncoded
    @POST("cards")
    Call<SimpleResponse> postNewCard(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Field("user_id") int userId,
            @Field("serial_number") String serialNumber,
            @Field("card_name") String cardName,
            @Field("card_icon") int cardIcon
    );

    @FormUrlEncoded
    @PUT("provider/unbind")
    Call<SimpleResponse> unbindProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Field("provider_id") int provderid
    );

    @FormUrlEncoded
    @POST("provider/bind")
    Call<SimpleResponse> bindProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Field("card_id") int cardId,
            @Field("provider_id") int provderid
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "provider/delete", hasBody = true)
    Call<SimpleResponse> deleteProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Field("provider_id") int provderid
    );

    @FormUrlEncoded
    @POST("entry/{userId}/{providerId}")
    Call<SimpleResponse> entryToProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("userId") int userid,
            @Path("providerId") int providerid,
            @Field("serial_number") String serialNumber
    );

    @FormUrlEncoded
    @PUT("user/pin/:userid")
    Call<SimpleResponse> updatePin(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") int elpin,
            @Field("new_pin") int newpin
    );

}
