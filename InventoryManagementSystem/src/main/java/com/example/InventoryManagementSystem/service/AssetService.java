package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.model.Asset;
import com.example.InventoryManagementSystem.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset addAsset(Asset asset) {
        return assetRepository.save(asset);
    }
}
