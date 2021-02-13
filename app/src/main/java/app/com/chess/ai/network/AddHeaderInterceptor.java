package app.com.chess.ai.network;

import androidx.annotation.NonNull;


import app.com.chess.ai._AppController;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeaderInterceptor implements Interceptor {


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        builder.header("Authorization", "Bearer " + _AppController.Companion.getToken());
        return chain.proceed(builder.build());
    }
}