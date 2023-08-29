package com.example.InventoryManagementSystem.dto.request;

import com.example.InventoryManagementSystem.model.EStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetRequest {
    private String typeName;
    private String assetName;
    private EStatus assetStatuses;
}
