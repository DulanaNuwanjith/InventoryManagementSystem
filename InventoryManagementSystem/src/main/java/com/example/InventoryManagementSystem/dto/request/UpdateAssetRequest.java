package com.example.InventoryManagementSystem.dto.request;

import com.example.InventoryManagementSystem.model.EStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAssetRequest {
    private EStatus status;
    private Long user;
}
