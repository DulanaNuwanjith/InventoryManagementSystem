package com.example.InventoryManagementSystem.controller;


import com.example.InventoryManagementSystem.dto.request.AssetRequest;
import com.example.InventoryManagementSystem.dto.response.ApiResponse;
import com.example.InventoryManagementSystem.model.Asset;
import com.example.InventoryManagementSystem.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAsset(@RequestBody AssetRequest assetRequest) {
        try {
            Asset newAsset = new Asset();
            newAsset.setAssetName(assetRequest.getAssetName());
            newAsset.setAssetType(assetRequest.getAssetType());

            Asset addedAsset = assetService.addAsset(newAsset);

            ApiResponse response = new ApiResponse(
                    true,
                    "Asset added successfully",
                    addedAsset.getId(),
                    addedAsset.getAssetName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(
                    false,
                    "Failed to add asset",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}