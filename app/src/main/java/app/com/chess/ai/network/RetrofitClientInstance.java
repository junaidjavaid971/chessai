package app.com.chess.ai.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static GetDataService service;

    private static Retrofit getRetrofitInstance(String baseUrl, boolean createNew) {
        if (createNew) {
            retrofit = null;
        }
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson));
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.connectTimeout(3, TimeUnit.MINUTES);
            okHttpClientBuilder.readTimeout(3, TimeUnit.MINUTES);
            okHttpClientBuilder.writeTimeout(3, TimeUnit.MINUTES);
            okHttpClientBuilder.addInterceptor(interceptor);
            //okHttpClientBuilder.addNetworkInterceptor(new AddHeaderInterceptor());
            /*try {
                KeyPinStore keyPinStore = new KeyPinStore();
                okHttpClientBuilder = keyPinStore.pinCertificate(_AppController.Companion.getINSTANCE(), okHttpClientBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            builder.client(okHttpClientBuilder.build());
            retrofit = builder.build();
        }

        return retrofit;
    }

    public static GetDataService getRetService(String url) {
       /* if (service == null) {

        }*/
        boolean b = true;
        if (retrofit != null) {
            b = !retrofit.baseUrl().url().toString().equals(url);
        }
        service = getRetrofitInstance(url, b).create(GetDataService.class);

        return service;
    }


    private static X509TrustManager getTrustManager() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
// SSLContext sslContext = SSLContext.getInstance("SSL");
// sslContext.init(null, new TrustManager[]{trustManager}, null);
        return trustManager;
    }
}