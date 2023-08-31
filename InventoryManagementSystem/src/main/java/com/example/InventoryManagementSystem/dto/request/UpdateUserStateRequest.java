package com.example.InventoryManagementSystem.dto.request;

import com.example.InventoryManagementSystem.model.UserState;
import lombok.Getter;

@Getter
public class UpdateUserStateRequest {

    private UserState newState;

    public void setNewState(UserState newState) {
        this.newState = newState;
    }
}
