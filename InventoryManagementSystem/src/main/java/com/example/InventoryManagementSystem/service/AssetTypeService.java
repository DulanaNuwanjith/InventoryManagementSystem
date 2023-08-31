package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.request.AssetTypeRequest;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.repository.AssetTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeRepository assetTypeRepository;
    private static final Logger logger = LoggerFactory.getLogger(AssetTypeService.class);

    @Autowired
    public AssetTypeService(AssetTypeRepository assetTypeRepository) {
        this.assetTypeRepository = assetTypeRepository;
    }
    public AssetType addAssetType(AssetTypeRequest assetType) {
        logger.info("Adding asset type with name: {}", assetType.getTypeName());

        AssetType newAssetType = new AssetType();
        Long nextId = getNextAssetTypeId();
        logger.info("Next asset type ID: {}", nextId);

        newAssetType.setTypeId(nextId);
        newAssetType.setTypeName(assetType.getTypeName());

        AssetType savedAssetType = assetTypeRepository.save(newAssetType);

        logger.info("Asset type added successfully with ID: {}", savedAssetType.getTypeId());

        return savedAssetType;
    }

    private Long getNextAssetTypeId() {
        logger.info("Fetching next asset type ID.");

        List<AssetType> allAssetTypes = assetTypeRepository.findAll();
        if (allAssetTypes.isEmpty()) {
            logger.info("No existing asset types. Assigning ID: 1");
            return 1L;
        }

        Long lastId = allAssetTypes.get(allAssetTypes.size() - 1).getTypeId();
        logger.info("Last asset type ID: {}", lastId);

        Long nextId = lastId + 1;
        logger.info("Next asset type ID: {}", nextId);

        return nextId;
    }

    public boolean deleteAssetType(long typeId) {
        logger.info("Deleting asset type with typeId: {}", typeId);

        AssetType assetType = assetTypeRepository.findByTypeId(typeId);

        if (assetType != null) {
            assetTypeRepository.delete(assetType);
            logger.info("Asset type with typeId {} deleted successfully.", typeId);
            return true;
        } else {
            logger.warn("Asset type with typeId {} not found for deletion.", typeId);
            return false;
        }
    }

    public AssetType updateAssetType(long typeId, AssetTypeRequest updatedAssetType) {
        logger.info("Updating asset type with typeId: {}", typeId);

        AssetType existingAssetType = assetTypeRepository.findByTypeId(typeId);

        if (existingAssetType != null) {
            String newTypeName = updatedAssetType.getTypeName();
            String currentTypeName = existingAssetType.getTypeName();

            if (!currentTypeName.equals(newTypeName) && assetTypeExistsByName(newTypeName)) {
                logger.warn("Asset type name '{}' already exists.", newTypeName);
                return null; // Return null to indicate the update is not allowed
            }

            existingAssetType.setTypeName(newTypeName);
            AssetType updatedType = assetTypeRepository.save(existingAssetType);
            logger.info("Updated asset type with typeId {} successfully.", typeId);
            return updatedType;
        } else {
            logger.warn("Asset type with typeId {} not found for update.", typeId);
            return null;
        }
    }


    public List<AssetType> findAllAssetTypes() {
        logger.info("Fetching all asset types.");
        return assetTypeRepository.findAll();
    }

    public AssetType findAssetTypeByTypeId(long typeId) {
        logger.info("Fetching asset type by typeId: {}", typeId);
        return assetTypeRepository.findByTypeId(typeId);
    }

    public boolean existsByTypeName(String typeName) {
        logger.info("Checking if asset type exists with typeName: {}", typeName);
        return assetTypeRepository.existsByTypeName(typeName);
    }



    public AssetType getAssetTypeByName(String typeName) {
        logger.info("Fetching asset type by name: {}", typeName);

        Optional<AssetType> assetType = assetTypeRepository.findByTypeName(typeName);

        if (assetType.isPresent()) {
            logger.info("Fetched asset type by name: {}", typeName);
            return assetType.get();
        } else {
            logger.warn("Asset type not found for name: {}", typeName);
            throw new IllegalArgumentException("Asset type not found.");
        }
    }

    public boolean assetTypeExistsByName(String typeName) {
        logger.info("Checking if asset type exists with typeName: {}", typeName);
        return assetTypeRepository.existsByTypeName(typeName);
    }
    
}

