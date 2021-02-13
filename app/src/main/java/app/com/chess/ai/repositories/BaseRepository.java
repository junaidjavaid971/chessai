package app.com.chess.ai.repositories;

import app.com.chess.ai.interfaces.RequestCallback;
import app.com.chess.ai.models.response.ResponseModel;
import app.com.chess.ai.network.GetDataService;
import app.com.chess.ai.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRepository {

    private static final String BASE_URL = "";


    protected GetDataService apiService = RetrofitClientInstance.getRetService(BASE_URL);
    public static int _zdks = -9999;


    protected <t> void registerApiRequest(final RequestCallback callback, Call<t> call) {
        call.enqueue(new Callback<t>() {

            @Override
            public void onResponse(Call<t> call, Response<t> response) {
                if (response.code() == 200) {
                    ResponseModel<t> responseModel = (ResponseModel<t>) response.body();
                    callback.onResponse(responseModel,response.code());
                } else {
                    callback.onResponse(new ResponseModel<>(false, String.valueOf(response.code()), response.message(), null, 1),response.code());
                }
            }

            @Override
            public void onFailure(Call<t> call, Throwable t) {

                callback.onResponse(new ResponseModel<>(false, String.valueOf(-1), t.getMessage(), null, 0), _zdks);
            }
        });
    }


}
