package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.repository.AssetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeRepository assetTypeRepository;

    public AssetType addAssetType(AssetType assetType) {

        Long nextId = getNextAssetTypeId();
        assetType.setId(nextId);

        return assetTypeRepository.save(assetType);
    }
    private Long getNextAssetTypeId() {
        List<AssetType> allAssetTypes = assetTypeRepository.findAll();
        if (allAssetTypes.isEmpty()) {
            return 1L;
        }

        Long lastId = allAssetTypes.get(allAssetTypes.size() - 1).getId();
        return lastId + 1;
    }

    public boolean deleteAssetType(Long id) {
        Optional<AssetType> assetTypeOptional = assetTypeRepository.findById(id);

        if (assetTypeOptional.isPresent()) {
            assetTypeRepository.delete(assetTypeOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public AssetType updateAssetType(AssetType assetType) {
        AssetType existingAssetType = assetTypeRepository.findById(assetType.getId()).orElse(null);
        if (existingAssetType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset type not found");
        }

        existingAssetType.setTypeName(assetType.getTypeName());

        return assetTypeRepository.save(existingAssetType);
    }

    public AssetType getAssetTypeById(long id) {
        return assetTypeRepository.findById(id).orElse(null);
    }

}

