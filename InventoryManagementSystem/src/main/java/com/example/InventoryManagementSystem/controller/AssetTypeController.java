package com.example.InventoryManagementSystem.controller;

import com.example.InventoryManagementSystem.dto.request.AssetTypeRequest;
import com.example.InventoryManagementSystem.dto.response.ApiResponse;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.service.AssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/asset-types")
public class AssetTypeController {

    @Autowired
    private AssetTypeService assetTypeService;

    @Autowired
    public AssetTypeController(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AssetType>> getAllAssetTypes() {
        List<AssetType> assetTypes = assetTypeService.findAllAssetTypes();
        return ResponseEntity.ok(assetTypes);
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<Object> getAssetTypeByTypeId(@PathVariable long typeId) {
        AssetType assetType = assetTypeService.findAssetTypeByTypeId(typeId);

        if (assetType != null) {
            return ResponseEntity.ok(assetType);
        } else {
            String errorMessage = "Asset type with typeId " + typeId + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAssetType(@RequestBody AssetTypeRequest assetTypeRequest) {
        try {
            if (assetTypeService.existsByTypeName(assetTypeRequest.getTypeName())) {
                ApiResponse response = new ApiResponse(false, "Asset type name already exists", null, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            AssetType addedAssetType = assetTypeService.addAssetType(assetTypeRequest);

            ApiResponse response = new ApiResponse(true, "Asset type added successfully", addedAssetType.getTypeId(), addedAssetType.getTypeName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Failed to add asset type", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{typeId}")
    public ResponseEntity<String> deleteAssetTypeByTypeId(@PathVariable long typeId) {
        boolean deleted = assetTypeService.deleteAssetType(typeId);

        if (deleted) {
            return ResponseEntity.ok("Asset type with typeId " + typeId + " deleted.");
        } else {
            String errorMessage = "Asset type with typeId " + typeId + " not found for deletion.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<AssetType> updateAssetType(@PathVariable long typeId, @RequestBody AssetTypeRequest updatedAssetType) {
        AssetType updatedType = assetTypeService.updateAssetType(typeId, updatedAssetType);

        if (updatedType != null) {
            return ResponseEntity.ok(updatedType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
