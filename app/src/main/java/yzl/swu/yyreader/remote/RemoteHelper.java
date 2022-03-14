package yzl.swu.yyreader.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import yzl.swu.yyreader.App;
import yzl.swu.yyreader.common.Constants;

public class RemoteHelper {
    private static final String TAG = "RemoteHelper";
    private static RemoteHelper sInstance;
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    private HashSet<String> cookies;

    private RemoteHelper() {
        mOkHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        chain -> {
                            Request request = chain.request();
                            Request.Builder builder = request.newBuilder();
                            if (cookies == null) {
                                SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(Constants.COOKIE_DATA, Context.MODE_PRIVATE);
                                String token = sharedPreferences.getString(Constants.COOKIE_KEY,null);
                                if (token != null){
                                    cookies = new HashSet<>();
                                    cookies.add(token);
                                }

                            }
                            if (cookies != null){
                                for (String cookie : cookies) {
                                    builder.addHeader(Constants.COOKIE_KEY, cookie);
                                }
                            }
                            request = builder.build();

                            //在这里获取到request后就可以做任何事情了
                            Response response = chain.proceed(request);

                            Log.d(TAG, "intercept: " + request.url().toString());
                            return response;
                        }
                ).build();


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.API_BASE_URL)
                .build();
    }

    public static RemoteHelper getInstance() {
        if (sInstance == null) {
            synchronized (RemoteHelper.class) {
                if (sInstance == null) {
                    sInstance = new RemoteHelper();
                }
            }
        }
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}

