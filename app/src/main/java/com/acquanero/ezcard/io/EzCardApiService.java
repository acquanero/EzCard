package com.acquanero.ezcard.io;

import com.acquanero.ezcard.io.ExternalRequestsModels.BindProviderRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.DeleteCardRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.DeleteProviderRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.EditCardRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.EditProviderNameRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.EntryRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.LoginRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.NewCardRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.RegisterRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.UnbindProviderRequest;
import com.acquanero.ezcard.io.ExternalRequestsModels.UpdatePinRequest;
import com.acquanero.ezcard.models.SimpleResponse;
import com.acquanero.ezcard.models.Token;
import com.acquanero.ezcard.models.UserData;
import com.acquanero.ezcard.models.UserIdToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//interface que define las rutas a la API

public interface EzCardApiService {

    @Headers("Content-Type: application/json")
    @POST("user/login")
    Call<UserIdToken> postDataGetToken(
            @Header("xappid") String xappid,
            @Body LoginRequest loginRequest

    );

    @Headers("Content-Type: application/json")
    @POST("user/login/{id}")
    Call<Token> logInWithToken(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("id") int id
    );

    @Headers("Content-Type: application/json")
    @GET("user/{id}")
    Call<UserData> getUserData(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("id") int id
    );

    @Headers("Content-Type: application/json")
    @POST("user/register")
    Call<UserIdToken> postToRegister(
            @Header("xappid") String xappid,
            @Body RegisterRequest registerRequest
    );

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "cards/{id}", hasBody = true)
    Call<SimpleResponse> deleteCard(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("id") int id,
            @Body DeleteCardRequest deleteCardRequest
    );

    @Headers("Content-Type: application/json")
    @PUT("cards/{id}")
    Call<SimpleResponse> putCard(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("id") int id,
            @Body EditCardRequest editCardRequest
    );

    @Headers("Content-Type: application/json")
    @POST("cards")
    Call<SimpleResponse> postNewCard(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Body NewCardRequest newCardJson
    );

    @Headers("Content-Type: application/json")
    @PUT("provider/unbind/{id}")
    Call<SimpleResponse> unbindProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("id") int userId,
            @Body UnbindProviderRequest unbindProviderRequest
    );

    @Headers("Content-Type: application/json")
    @POST("provider/bind/{id}")
    Call<SimpleResponse> bindProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("id") int userId,
            @Body BindProviderRequest bindProviderRequest
            );

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "provider/delete/{id}", hasBody = true)
    Call<SimpleResponse> deleteProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("id") int userId,
            @Body DeleteProviderRequest deleteProviderRequest
            );

    @Headers("Content-Type: application/json")
    @POST("entry/{providerId}")
    Call<SimpleResponse> entryToProvider(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("providerId") int providerid,
            @Body EntryRequest entryRequest
    );

    @Headers("Content-Type: application/json")
    @PUT("user/pin/{userId}")
    Call<SimpleResponse> updatePin(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("userId") int userid,
            @Body UpdatePinRequest updatePinRequest
            );

    @Headers("Content-Type: application/json")
    @PUT("provider/name/{userId}")
    Call<SimpleResponse> putProviderNewName(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Header("pin") String elpin,
            @Path("userId") int userid,
            @Body EditProviderNameRequest editProviderNameRequest
    );
}
