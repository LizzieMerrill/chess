package server;

import com.google.gson.JsonObject;
import dataAccess.data.AuthData;
import dataAccess.data.UserData;

import java.util.List;
import java.util.Objects;

public class StandardResponse {
    private int status;
    private String message;

    //private final UserData userData;
    private Object data = null;
    private List<JsonObject> dataList;

    public StandardResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public StandardResponse(int status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public StandardResponse(int status, Object data){
        this.status = status;
        this.data = data;
    }
    public StandardResponse(String message){
        this.message = message;
    }

    public StandardResponse(int status, List<JsonObject> dataList){
        this.status = status;
        this.dataList = dataList;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardResponse that = (StandardResponse) o;
        return status == that.status && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }
}
