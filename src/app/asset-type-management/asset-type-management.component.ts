import { Component, OnInit } from '@angular/core';
import { AssetTypeService } from '../_services/asset-type.service';
import { NgForm } from '@angular/forms';

interface AssetType {
  typeId: number;
  typeName: string;
  isEditing: boolean;
  updatedName: string;
}

@Component({
  selector: 'app-asset-type-management',
  templateUrl: './asset-type-management.component.html',
  styleUrls: ['./asset-type-management.component.css']
})
export class AssetTypeManagementComponent implements OnInit {
  assetTypes: AssetType[] = [];
  searchAssetTypeText: string = '';
  suggestions: string[] = [];
  filteredAssetTypes: AssetType[] = [];

  assetTypeData = {
    typeName: '',
  };

  constructor(private assetTypeService: AssetTypeService) { }

  ngOnInit(): void {
    this.loadAssetType();
  }

  loadAssetType() {
    this.assetTypeService.getAllAssetTypes().subscribe((data: AssetType[]) => {
      this.assetTypes = data.map(assetType => ({ ...assetType, isEditing: false, updatedName: assetType.typeName }));
      this.filteredAssetTypes = [...this.assetTypes];
    });
  }

  addAssetType(addAssetTypeForm: NgForm) {
    this.assetTypeService.addAssetType(this.assetTypeData)
      .subscribe(
        (response) => {
          console.log('Asset type Add successful:', response);
          addAssetTypeForm.resetForm();
          this.loadAssetType();
        },
        (error) => {
          console.error('Asset type Add failed:', error);
        }
      );
  }

  searchAssetType() {
    if (this.searchAssetTypeText.trim() === '') {
      this.loadAssetType();
    } else {
      this.filteredAssetTypes = this.assetTypes.filter((assetType: any) => {
        return Object.values(assetType).some((value: any) => {
          if (typeof value === 'string' || typeof value === 'number') {
            return value.toString().toLowerCase().includes(this.searchAssetTypeText.toLowerCase());
          }
          return false;
        });
      });
    }
  }


  selectSuggestion(suggestion: string) {
    this.searchAssetTypeText = suggestion;
    this.searchAssetType();
    this.suggestions = [];
  }

  showSuggestions() {
    this.suggestions = [
      `Suggestion 1 for ${this.searchAssetTypeText}`,
      `Suggestion 2 for ${this.searchAssetTypeText}`,
    ];
  }

  deleteAssetTypeByTypeId(typeId: number) {
    this.assetTypeService.deleteAssetTypeByTypeId(typeId).subscribe(
      (response) => {
        console.log('Asset type deleted successfully:', response);
        this.assetTypes = this.assetTypes.filter((assetType) => assetType.typeId !== typeId);
        this.filteredAssetTypes = this.filteredAssetTypes.filter((assetType) => assetType.typeId !== typeId);
      },
      (error) => {
        console.error('Asset type deletion failed:', error);
      }
    );
  }
}
