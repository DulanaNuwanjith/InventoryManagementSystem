package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.request.AssetTypeRequest;
import com.example.InventoryManagementSystem.model.AssetType;
import com.example.InventoryManagementSystem.repository.AssetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeRepository assetTypeRepository;

    @Autowired
    public AssetTypeService(AssetTypeRepository assetTypeRepository) {
        this.assetTypeRepository = assetTypeRepository;
    }
public AssetType addAssetType(AssetTypeRequest assetType) {
    AssetType newAssetType = new AssetType();
    Long nextId = getNextAssetTypeId();
    System.out.println(nextId);
    newAssetType.setTypeId(nextId);
    newAssetType.setTypeName(assetType.getTypeName());

    return assetTypeRepository.save(newAssetType);
}
    private Long getNextAssetTypeId() {
        List<AssetType> allAssetTypes = assetTypeRepository.findAll();
        if (allAssetTypes.isEmpty()) {
            return 1L;
        }

        Long lastId = allAssetTypes.get(allAssetTypes.size() - 1).getTypeId();
        System.out.println(lastId);
        return lastId + 1;
    }
    public boolean deleteAssetType(long typeId) {
        AssetType assetType = assetTypeRepository.findByTypeId(typeId);

        if (assetType != null) {
            assetTypeRepository.delete(assetType);
            return true;
        } else {
            return false;
        }
    }

    public AssetType updateAssetType(long typeId, AssetTypeRequest updatedAssetType) {
        AssetType existingAssetType = assetTypeRepository.findByTypeId(typeId);

        if (existingAssetType != null) {
            existingAssetType.setTypeName(updatedAssetType.getTypeName());
            return assetTypeRepository.save(existingAssetType);
        } else {
            return null;
        }
    }

    public List<AssetType> findAllAssetTypes() {
        return assetTypeRepository.findAll();
    }

    public AssetType findAssetTypeByTypeId(long typeId) {
        return assetTypeRepository.findByTypeId(typeId);
    }

    public boolean existsByTypeName(String typeName) {
        return assetTypeRepository.existsByTypeName(typeName);
    }

    public AssetType getAssetTypeById(long id) {
    //  TODO: Map the correct asset type from DB
    return new AssetType();
//        return assetTypeRepository.findById(id);
    }



}

