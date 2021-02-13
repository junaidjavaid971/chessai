package app.com.chess.ai.interfaces;


import app.com.chess.ai.models.response.ResponseModel;

public abstract class RequestCallback {
    public <t> void onResponse(ResponseModel<t> response, int code){};
    public void onResponse(Boolean isSuccess, int code){};
}
