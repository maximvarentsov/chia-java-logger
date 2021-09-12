package pw.gatchina.okex.client;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HttpResult {
    public int code;
    public String msg;
    public String detailMsg;
    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_message")
    public String errorMessage;
}

