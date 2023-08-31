package com.example.InventoryManagementSystem.controller;

import com.example.InventoryManagementSystem.dto.request.AssetRequest;
import com.example.InventoryManagementSystem.dto.request.UpdateAssetRequest;
import com.example.InventoryManagementSystem.model.Asset;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.model.EStatus;
import com.example.InventoryManagementSystem.service.AssetService;
import com.example.InventoryManagementSystem.service.AssetTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final AssetTypeService assetTypeService;
    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);


    @Autowired
    public AssetController(AssetService assetService, AssetTypeService assetTypeService) {
        this.assetService = assetService;
        this.assetTypeService = assetTypeService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAsset(
            @RequestBody AssetRequest assetRequest
    ) {
        logger.info("Received request to add asset with name: {}", assetRequest.getAssetName());

        if (!assetTypeService.assetTypeExistsByName(assetRequest.getTypeName())) {
            logger.warn("Invalid asset type name: {}", assetRequest.getTypeName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid asset type name.");
        }

        String assetName = assetRequest.getAssetName();
        EStatus assetStatus = assetRequest.getAssetStatuses();

        if (assetName.length() < 4) {
            logger.warn("Asset name '{}' must have at least 4 characters.", assetName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Asset name must have at least 4 characters.");
        }
        if (assetService.existsAssetWithAssetName(assetRequest.getAssetName())) {
            logger.warn("Asset with name '{}' already exists.", assetName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Asset with the same name already exists.");
        }

        AssetType assetType = assetTypeService.getAssetTypeByName(assetRequest.getTypeName());

        try {
            Asset addedAsset = assetService.addAsset(assetRequest.getAssetName(), assetType, assetStatus);

            logger.info("Asset added successfully with ID: {}, assetId: {}", addedAsset.getId(), addedAsset.getAssetId());
            String message = "Asset added successfully with ID: " + addedAsset.getId()
                    + ", assetId: " + addedAsset.getAssetId();
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add asset: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{assetId}")
    public ResponseEntity<String> deleteAsset(@PathVariable UUID assetId) {
        logger.info("Received request to delete asset with assetId: {}", assetId);

        boolean deleted = assetService.deleteAssetByAssetId(assetId);

        if (deleted) {
            logger.info("Asset with assetId {} has been deleted.", assetId);
            return ResponseEntity.ok("Asset with assetId " + assetId + " has been deleted.");
        } else {
            logger.warn("Asset with assetId {} not found for deletion.", assetId);
            String errorMessage = "Asset with assetId " + assetId + " not found for deletion.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<String> updateAsset(@PathVariable UUID assetId, @RequestBody UpdateAssetRequest request) {
        logger.info("Received request to update asset with assetId: {}", assetId);

        boolean updated = assetService.updateAsset(assetId, request);

        if (updated) {
            logger.info("Asset with assetId {} has been updated successfully.", assetId);
            return ResponseEntity.ok("Asset updated successfully");
        } else {
            logger.warn("Asset with assetId {} not found for update.", assetId);
            String message = "Asset not found for assetId: " + assetId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }


    @GetMapping("/by-status")
    public ResponseEntity<Map<EStatus, ArrayList<Asset>>> getAssetsByStatus() {
        logger.info("Received request to get assets by status.");

        Map<EStatus, ArrayList<Asset>> assetsByStatus = assetService.getAssetsByStatus();

        if (assetsByStatus.isEmpty()) {
            logger.warn("No assets found by status.");
        } else {
            logger.info("Retrieved assets by status successfully.");
        }

        return ResponseEntity.ok(assetsByStatus);
    }


    @GetMapping("/view/{assetId}")
    public ResponseEntity<Object> getAssetByAssetId(@PathVariable UUID assetId) {
        logger.info("Received request to get asset by assetId: {}", assetId);

        Asset asset = assetService.getAssetByAssetId(assetId);

        if (asset != null) {
            logger.info("Retrieved asset with assetId {}: {}", assetId, asset);
            return ResponseEntity.ok(asset);
        } else {
            logger.warn("Asset not found for assetId: {}", assetId);
            String message = "Asset not found for assetId: " + assetId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @GetMapping("/my/assets")
    public ResponseEntity<List<Asset>> viewUserAssets(@RequestParam Long userId) {
        List<Asset> userAssets = assetService.getAssetsByUser(userId);

        if (userAssets.isEmpty()) {
            return ResponseEntity.notFound().build(); // No assets found
        }

        return ResponseEntity.ok(userAssets); // Assets found
    }


}