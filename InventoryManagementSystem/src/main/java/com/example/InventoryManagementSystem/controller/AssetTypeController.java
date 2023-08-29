package com.example.InventoryManagementSystem.controller;

import com.example.InventoryManagementSystem.dto.request.AssetTypeRequest;
import com.example.InventoryManagementSystem.dto.response.ApiResponse;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.service.AssetTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final AssetTypeService assetTypeService;
    private static final Logger logger = LoggerFactory.getLogger(AssetTypeController.class);


    @Autowired
    public AssetTypeController(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AssetType>> getAllAssetTypes() {
        logger.info("Received request to get all asset types.");

        List<AssetType> assetTypes = assetTypeService.findAllAssetTypes();

        logger.info("Retrieved {} asset types.", assetTypes.size());

        return ResponseEntity.ok(assetTypes);
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<Object> getAssetTypeByTypeId(@PathVariable long typeId) {
        logger.info("Received request to get asset type by typeId: {}", typeId);

        AssetType assetType = assetTypeService.findAssetTypeByTypeId(typeId);

        if (assetType != null) {
            logger.info("Retrieved asset type with typeId {}: {}", typeId, assetType);
            return ResponseEntity.ok(assetType);
        } else {
            logger.warn("Asset type with typeId {} not found.", typeId);
            String errorMessage = "Asset type with typeId " + typeId + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAssetType(@RequestBody AssetTypeRequest assetTypeRequest) {
        logger.info("Received request to add asset type with name: {}", assetTypeRequest.getTypeName());

        try {
            if (assetTypeService.existsByTypeName(assetTypeRequest.getTypeName())) {
                logger.warn("Asset type name '{}' already exists.", assetTypeRequest.getTypeName());
                ApiResponse response = new ApiResponse(false, "Asset type name already exists", null, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            AssetType addedAssetType = assetTypeService.addAssetType(assetTypeRequest);

            logger.info("Asset type added successfully with typeId: {}, typeName: {}", addedAssetType.getTypeId(), addedAssetType.getTypeName());
            ApiResponse response = new ApiResponse(true, "Asset type added successfully", addedAssetType.getTypeId(), addedAssetType.getTypeName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to add asset type: {}", e.getMessage());
            ApiResponse response = new ApiResponse(false, "Failed to add asset type", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @DeleteMapping("/{typeId}")
    public ResponseEntity<String> deleteAssetTypeByTypeId(@PathVariable long typeId) {
        logger.info("Received request to delete asset type with typeId: {}", typeId);

        boolean deleted = assetTypeService.deleteAssetType(typeId);

        if (deleted) {
            logger.info("Asset type with typeId {} has been deleted.", typeId);
            return ResponseEntity.ok("Asset type with typeId " + typeId + " deleted.");
        } else {
            logger.warn("Asset type with typeId {} not found for deletion.", typeId);
            String errorMessage = "Asset type with typeId " + typeId + " not found for deletion.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    @PutMapping("/{typeId}")
    public ResponseEntity<Object> updateAssetType(@PathVariable long typeId, @RequestBody AssetTypeRequest updatedAssetType) {
        logger.info("Received request to update asset type with typeId: {}", typeId);

        AssetType updatedType = assetTypeService.updateAssetType(typeId, updatedAssetType);

        if (updatedType != null) {
            logger.info("Asset type with typeId {} has been updated successfully.", typeId);
            return ResponseEntity.ok(updatedType);
        } else {
            logger.warn("Failed to update asset type with typeId {}. A type with the provided name already exists.", typeId);
            return ResponseEntity
                    .badRequest()
                    .body("Failed to update asset type. A type with the provided name already exists.");
        }
    }

}
