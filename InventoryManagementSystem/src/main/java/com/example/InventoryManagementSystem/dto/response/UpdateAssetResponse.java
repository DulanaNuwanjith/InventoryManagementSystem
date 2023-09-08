package com.example.InventoryManagementSystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateAssetResponse {


    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    public UpdateAssetResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
