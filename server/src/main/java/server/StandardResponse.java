package server;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.Objects;

public class StandardResponse {
    private int status;
    private String message;

    private Object data = null;
    private List<JsonObject> dataList;

    public StandardResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public StandardResponse(String message){
        this.message = message;
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
