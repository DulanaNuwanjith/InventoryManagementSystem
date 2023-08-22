package com.example.InventoryManagementSystem.repository;

import com.example.InventoryManagementSystem.model.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetRepository extends MongoRepository<Asset, Long> {

}
