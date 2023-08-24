package com.example.InventoryManagementSystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document(collection = "assetTypes")
public class AssetType {

    @Id
    private Long id;

    @Indexed(unique = true)
    private String typeName;

    private Map<AssetType, List<Asset>> assets = new HashMap<>();
}
