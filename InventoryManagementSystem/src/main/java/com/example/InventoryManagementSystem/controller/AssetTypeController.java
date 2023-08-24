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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api")
public class AssetTypeController {

    @Autowired
    private AssetTypeService assetTypeService;

    @GetMapping("/asset-types/add")
    public String showAddAssetTypeForm(Model model) {
        model.addAttribute("assetTypeRequest", new AssetTypeRequest());
        return "add-asset-type";
    }

    @PostMapping("/asset-types/add")
    public ResponseEntity<ApiResponse> addAssetType(@RequestBody AssetTypeRequest assetTypeRequest) {
        try {
            AssetType newAssetType = new AssetType();
            newAssetType.setTypeName(assetTypeRequest.getTypeName());

            AssetType addedAssetType = assetTypeService.addAssetType(newAssetType);

            ApiResponse response = new ApiResponse(true, "Asset type added successfully", addedAssetType.getId(), addedAssetType.getTypeName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Failed to add asset type", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @DeleteMapping("/asset-types/{id}")
    public ResponseEntity<ApiResponse> deleteAssetType(@PathVariable Long id) {
        try {
            boolean isDeleted = assetTypeService.deleteAssetType(id);

            if (isDeleted) {
                ApiResponse response = new ApiResponse(true, "Asset type deleted successfully", null, null);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse response = new ApiResponse(false, "Asset type not found or failed to delete", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Failed to delete asset type", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/asset-types/update/{id}")
    public ResponseEntity<ApiResponse> updateAssetType(@PathVariable Long id, @RequestBody AssetTypeRequest assetTypeRequest) {
        try {
            // Assuming assetTypeService.getAssetTypeById(id) retrieves the existing asset type
            AssetType existingAssetType = assetTypeService.getAssetTypeById(id);

            if (existingAssetType == null) {
                ApiResponse notFoundResponse = new ApiResponse(false, "Asset type not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            existingAssetType.setTypeName(assetTypeRequest.getTypeName());

            AssetType updatedAssetType = assetTypeService.updateAssetType(existingAssetType);

            ApiResponse response = new ApiResponse(true, "Asset type updated successfully", updatedAssetType.getId(), updatedAssetType.getTypeName());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Failed to update asset type", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/asset-types/view/{id}")
    public ResponseEntity<ApiResponse> viewAssetType(@PathVariable Long id) {
        try {
            AssetType assetType = assetTypeService.getAssetTypeById(id);

            if (assetType == null) {
                ApiResponse notFoundResponse = new ApiResponse(false, "Asset type not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            ApiResponse response = new ApiResponse(true, "Asset type details retrieved successfully", assetType.getId(), assetType.getTypeName());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Failed to retrieve asset type details", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



}
