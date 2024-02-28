package server;

import dataAccess.data.UserData;

import java.util.Objects;

public class StandardResponse {
    private final int status;
    private final String message;

    //private final UserData userData;

    public StandardResponse(int status, String message) {
        this.status = status;
        this.message = message;
        //this.userData = userData;
    }
//    public StandardResponse(int status, String message){
//        this.status = status;
//        this.message = message;
//    }

//    public StandardResponse(int status, AuthData auth){
//        this.status = status;
//    }

    // Add getters if needed


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