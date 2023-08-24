package com.example.InventoryManagementSystem.dto.request;

import com.example.InventoryManagementSystem.model.AssetType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetRequest {
    private String assetName;
    private AssetType assetType;
}
