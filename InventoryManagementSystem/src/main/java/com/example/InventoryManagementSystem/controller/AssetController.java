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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<Map<String, Object>> addAsset(@RequestBody AssetRequest assetRequest) {
        Map<String, Object> response = new HashMap<>();

        logger.info("Received request to add asset with name: {}", assetRequest.getAssetName());

        ResponseEntity<Map<String, Object>> serviceResponse = assetService.addAsset(assetRequest);

        if (serviceResponse.getStatusCode().is2xxSuccessful()) {
            response.put("status", "success");
            response.put("message", serviceResponse.getBody());
        } else {
            response.put("status", "error");
            response.put("error", serviceResponse.getBody());
        }

        return ResponseEntity.status(serviceResponse.getStatusCode()).body(response);
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Map<String, Object>> deleteAsset(@PathVariable UUID assetId) {
        Map<String, Object> response = new HashMap<>();

        logger.info("Received request to delete asset with assetId: {}", assetId);

        ResponseEntity<String> serviceResponse = assetService.deleteAsset(assetId);

        if (serviceResponse.getStatusCode().is2xxSuccessful()) {
            response.put("status", "success");
            response.put("message", serviceResponse.getBody());
        } else {
            response.put("status", "error");
            response.put("error", serviceResponse.getBody());
        }

        return ResponseEntity.status(serviceResponse.getStatusCode()).body(response);
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<Object> updateAsset(@PathVariable UUID assetId, @RequestBody UpdateAssetRequest request) {
        logger.info("Received request to update asset with assetId: {}", assetId);
        boolean updated = assetService.updateAssetWithRequest(assetId, request);

        if (updated) {
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Asset updated successfully\"}");
        } else {
            String message = "Asset not found for assetId: " + assetId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"" + message + "\"}");
        }
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
    @GetMapping("/all")
    public ResponseEntity<List<Asset>> getAllAssetTypes() {
        logger.info("Received request to get all assets.");
        return assetService.getAllAsset();
    }

    @GetMapping("/byAssetTypeName/{typeName}")
    public List<Asset> getAssetsByAssetTypeName(@PathVariable String typeName) {
        return assetService.getAssetsByAssetTypeName(typeName);
    }

}