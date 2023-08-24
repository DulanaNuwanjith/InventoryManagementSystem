package com.example.InventoryManagementSystem.repository;

import com.example.InventoryManagementSystem.model.AssetType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTypeRepository extends MongoRepository<AssetType, Long> {

}
