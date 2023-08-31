package com.example.InventoryManagementSystem.controller;

import com.example.InventoryManagementSystem.dto.request.AssetRequest;
import com.example.InventoryManagementSystem.dto.request.UpdateAssetRequest;
import com.example.InventoryManagementSystem.model.Asset;
import com.example.InventoryManagementSystem.model.EStatus;
import com.example.InventoryManagementSystem.service.AssetService;
import com.example.InventoryManagementSystem.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);


    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAsset(@RequestBody AssetRequest assetRequest) {
        logger.info("Received request to add asset with name: {}", assetRequest.getAssetName());
        return assetService.addAsset(assetRequest);
    }

    @DeleteMapping("/delete/{assetId}")
    public ResponseEntity<String> deleteAsset(@PathVariable UUID assetId) {
        logger.info("Received request to delete asset with assetId: {}", assetId);
        return assetService.deleteAsset(assetId);
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<String> updateAsset(@PathVariable UUID assetId, @RequestBody UpdateAssetRequest request) {
        logger.info("Received request to update asset with assetId: {}", assetId);
        return assetService.updateAsset(assetId, request);
    }

    @GetMapping("/by-status")
    public ResponseEntity<Map<EStatus, ArrayList<Asset>>> getAssetsByStatus() {
        logger.info("Received request to get assets by status.");
        return assetService.getAssetsByStatus();
    }

    @GetMapping("/view/{assetId}")
    public ResponseEntity<Object> getAssetByAssetId(@PathVariable UUID assetId) {
        logger.info("Received request to get asset by assetId: {}", assetId);
        return assetService.getAssetByAssetId(assetId);
    }

    @GetMapping("/my/assets")
    public ResponseEntity<List<Asset>> viewCurrentUserAssets() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();

        return assetService.viewUserAssets(currentUserId);
    }
}