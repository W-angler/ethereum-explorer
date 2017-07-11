package com.w_angler.ethereum.explorer.response;

public enum Status {
    SUCCESS(200, "success"),
    ETH_GET_BLOCK_FAILED(1, "get block failed"),
    ETH_GET_TX_FAILED(2,"get transaction failed"),
    ETH_GET_LATEST_BLOCK_FAILED(3,"get latest block failed"),
    ETH_GET_LATEST_TX_FAILED(4,"get latest transaction failed");
    private int code;
    private String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
