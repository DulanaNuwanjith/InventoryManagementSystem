import { Component, OnInit } from '@angular/core';
import { AssetService } from '../_services/asset.service';
import { Asset } from '../_model/asset.model';
import { NgForm } from '@angular/forms';

// interface Asset {
//   assetId: number;
//   assetName: string;
//   assetStatus: string;
//   assetType: {
//     typeName: string;
//   };
//   user: number;
// }

@Component({
  selector: 'app-asset-management',
  templateUrl: './asset-management.component.html',
  styleUrls: ['./asset-management.component.css']
})

export class AssetManagementComponent implements OnInit {
  assets: Asset[] = [];
  filteredAssets: Asset[] = [];
  searchAssetText: string = '';
  suggestions: string[] = [];

  assetData = {
    assetName: '',
    typeName: '',
  };

  constructor(private assetService: AssetService) { }

  ngOnInit(): void {
    this.loadAsset();
  }

  loadAsset() {
    this.assetService.getAllAssets().subscribe((data: Asset[]) => {
      this.assets = data;
      this.filteredAssets = data;
    });
  }

  addAsset(addAssetForm: NgForm) {
    this.assetService.addAsset(this.assetData)
      .subscribe(
        (response) => {
          console.log('Asset type Add successful:', response);
          addAssetForm.resetForm();
          this.loadAsset();
        },
        (error) => {
          console.error('Asset type Add failed:', error);
        }
      );
  }

  deleteAssetById(assetId: number) {
    this.assetService.deleteAssetById(assetId).subscribe(
      (response) => {
        console.log('Asset type deleted successfully:', response);
        this.assets = this.assets.filter((asset) => asset.assetId !== assetId);
        this.filteredAssets = this.filteredAssets.filter((asset) => asset.assetId !== assetId);
      },
      (error) => {
        console.error('Asset deletion failed:', error);
      }
    );
  }

  searchAsset() {
    if (this.searchAssetText.trim() === '') {
      this.loadAsset();
    } else {
      this.filteredAssets = this.assets.filter((asset: any) => {
        return Object.values(asset).some((value: any) => {
          if (typeof value === 'string' || typeof value === 'number') {
            return value.toString().toLowerCase().includes(this.searchAssetText.toLowerCase());
          }
          return false;
        });
      });
    }
  }
  
  selectSuggestion(suggestion: string) {
    this.searchAssetText = suggestion;
    this.searchAsset();
    this.suggestions = [];
  }
  
  showSuggestions() {
    this.suggestions = [
      `Suggestion 1 for ${this.searchAssetText}`,
      `Suggestion 2 for ${this.searchAssetText}`,
    ];
  }
  


}
