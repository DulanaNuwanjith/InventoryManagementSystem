package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.request.AssetRequest;
import com.example.InventoryManagementSystem.dto.request.UpdateAssetRequest;
import com.example.InventoryManagementSystem.model.Asset;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.model.EStatus;
import com.example.InventoryManagementSystem.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetTypeService assetTypeService;
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
    @Autowired
    public AssetService(AssetRepository assetRepository, AssetTypeService assetTypeService) {
        this.assetRepository = assetRepository;
        this.assetTypeService = assetTypeService;
    }

    public ResponseEntity<String> addAsset(AssetRequest assetRequest) {
        String assetName = assetRequest.getAssetName();
        EStatus assetStatus = assetRequest.getAssetStatuses();

        if (!assetTypeService.assetTypeExistsByName(assetRequest.getTypeName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid asset type name.");
        }

        if (assetName.length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Asset name must have at least 4 characters.");
        }

        if (existsAssetWithAssetName(assetRequest.getAssetName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Asset with the same name already exists.");
        }

        AssetType assetType = assetTypeService.getAssetTypeByName(assetRequest.getTypeName());

        try {
            Asset addedAsset = addAsset(assetRequest.getAssetName(), assetType, assetStatus);

            String message = "Asset added successfully with ID: " + addedAsset.getId()
                    + ", assetId: " + addedAsset.getAssetId();
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public Asset addAsset(String assetName, AssetType assetType, EStatus assetStatus) {
        logger.info("Adding asset with name: {}, type: {}, status: {}", assetName, assetType.getTypeName(), assetStatus);

        UUID newAssetId = UUID.randomUUID();

        Asset asset = new Asset();
        asset.setAssetId(newAssetId);
        asset.setAssetName(assetName);
        asset.setAssetType(assetType);
        asset.setAssetStatus(EStatus.AVAILABLE);

        Asset savedAsset = assetRepository.save(asset);

        logger.info("Asset added successfully with ID: {}, assetId: {}", savedAsset.getId(), savedAsset.getAssetId());

        return savedAsset;
    }

    public boolean existsAssetWithAssetName(String assetName) {
        logger.info("Checking if asset exists with assetName: {}", assetName);
        return assetRepository.existsByAssetName(assetName);
    }

    public ResponseEntity<String> deleteAsset(UUID assetId) {
        boolean deleted = deleteAssetByAssetId(assetId);

        if (deleted) {
            return ResponseEntity.ok("Asset with assetId " + assetId + " has been deleted.");
        } else {
            String errorMessage = "Asset with assetId " + assetId + " not found for deletion.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }
    public boolean deleteAssetByAssetId(UUID assetId) {
        logger.info("Deleting asset with assetId: {}", assetId);

        Asset asset = assetRepository.findByAssetId(assetId);

        if (asset != null) {
            assetRepository.delete(asset);
            logger.info("Asset with assetId {} deleted successfully.", assetId);
            return true;
        } else {
            logger.warn("Asset with assetId {} not found for deletion.", assetId);
            return false;
        }
    }

    public ResponseEntity<String> updateAsset(UUID assetId, UpdateAssetRequest request) {
        boolean updated = updateAssetWithRequest(assetId, request);

        if (updated) {
            return ResponseEntity.ok("Asset updated successfully");
        } else {
            String message = "Asset not found for assetId: " + assetId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    public boolean updateAssetWithRequest(UUID assetId, UpdateAssetRequest request) {
        logger.info("Updating asset with assetId: {}", assetId);

        Asset asset = assetRepository.findByAssetId(assetId);
        if (asset == null) {
            logger.warn("Asset with assetId {} not found for update.", assetId);
            return false;
        }

        if (request.getStatus() != null) {
            asset.setAssetStatus(request.getStatus());
            logger.info("Updated asset status to: {} for assetId: {}", request.getStatus(), assetId);
        }

        if (request.getUser() != null) {
            asset.setUser(request.getUser());
            logger.info("Updated asset user to: {} for assetId: {}", request.getUser(), assetId);
        }

        assetRepository.save(asset);

        logger.info("Asset with assetId {} updated successfully.", assetId);

        return true;
    }
    public ResponseEntity<Map<EStatus, ArrayList<Asset>>> getAssetsByStatus() {
        logger.info("Fetching assets grouped by status.");

        List<Asset> allAssets = assetRepository.findAll();

        Map<EStatus, ArrayList<Asset>> assetsByStatus = allAssets.stream()
                .collect(Collectors.groupingBy(Asset::getAssetStatus,
                        Collectors.toCollection(ArrayList::new)));

        if (assetsByStatus.isEmpty()) {
            logger.warn("No assets found by status.");
            return ResponseEntity.notFound().build();
        } else {
            logger.info("Retrieved assets by status successfully.");
            return ResponseEntity.ok(assetsByStatus);
        }
    }

    public ResponseEntity<Object> getAssetByAssetId(UUID assetId) {
        logger.info("Fetching asset by assetId: {}", assetId);

        Asset asset = assetRepository.findByAssetId(assetId);

        if (asset != null) {
            logger.info("Fetched asset by assetId: {}", assetId);
            return ResponseEntity.ok(asset);
        } else {
            logger.warn("Asset not found for assetId: {}", assetId);
            String message = "Asset not found for assetId: " + assetId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
    public ResponseEntity<List<Asset>> viewUserAssets(Long userId) {
        logger.info("Fetching assets for user with userId: {}", userId);

        List<Asset> userAssets = getAssetsByUser(userId);

        if (userAssets.isEmpty()) {
            logger.warn("No assets found for user with userId: {}", userId);
            return ResponseEntity.notFound().build();
        }

        logger.info("Retrieved assets for user with userId: {}", userId);
        return ResponseEntity.ok(userAssets);
    }

    private List<Asset> getAssetsByUser(Long userId) {
        return assetRepository.findByUser(userId);
    }


}