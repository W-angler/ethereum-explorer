package com.w_angler.ethereum.explorer.response;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String message;
    private Object data;

    public Response(Status state, String message, Object data) {
        this.code = state.getCode();
        this.message = message;
        this.data = data;
    }
    public Response(Status state, Object data) {
        this.code = state.getCode();
        this.message = state.getMessage();
        this.data = data;
    }
    public Response(Status state) {
        this.code = state.getCode();
        this.message = state.getMessage();
        this.data = null;
    }
}
