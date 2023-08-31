package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.request.UpdateAssetRequest;
import com.example.InventoryManagementSystem.model.Asset;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.model.EStatus;
import com.example.InventoryManagementSystem.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
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

    public boolean updateAsset(UUID assetId, UpdateAssetRequest request) {
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


    public Map<EStatus, ArrayList<Asset>> getAssetsByStatus() {
        logger.info("Fetching assets grouped by status.");

        List<Asset> allAssets = assetRepository.findAll();

        Map<EStatus, ArrayList<Asset>> assetsByStatus = allAssets.stream()
                .collect(Collectors.groupingBy(Asset::getAssetStatus,
                        Collectors.toCollection(ArrayList::new)));

        logger.info("Fetched assets by status: {}", assetsByStatus);

        return assetsByStatus;
    }

    public Asset getAssetByAssetId(UUID assetId) {
        logger.info("Fetching asset by assetId: {}", assetId);

        Asset asset = assetRepository.findByAssetId(assetId);

        if (asset == null) {
            logger.warn("Asset not found for assetId: {}", assetId);
        } else {
            logger.info("Fetched asset by assetId: {}", assetId);
        }

        return asset;
    }


    public List<Asset> getAssetsByUser(Long userId) {
        return assetRepository.findByUser(userId);
    }

}