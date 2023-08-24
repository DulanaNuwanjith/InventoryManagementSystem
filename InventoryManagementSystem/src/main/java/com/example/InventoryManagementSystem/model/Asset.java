package com.example.InventoryManagementSystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter


@Document(collection = "assets")
public class Asset {

    @Id
    private Long id;
    private String assetName;
    private AssetType assetType;
}
