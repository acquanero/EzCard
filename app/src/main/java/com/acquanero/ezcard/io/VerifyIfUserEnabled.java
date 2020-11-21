package com.acquanero.ezcard.io;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.acquanero.ezcard.activities.LogInActivity;
import com.acquanero.ezcard.models.SimpleResponse;

import retrofit2.Response;

public class VerifyIfUserEnabled {
    public static void verifyUserEnabled(Response<SimpleResponse> response, Context context, Activity activity) {
        if (response.code() == 401) {
            Intent intent = new Intent(context.getApplicationContext(), LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
