import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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
  selectedAsset: Asset | null = null;
  originalAsset: Asset | null = null;

  assetData = {
    assetName: '',
    typeName: '',
  };

  constructor(private assetService: AssetService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadAsset();
  }

  loadAsset() {
    this.assetService.getAllAssets().subscribe((data: Asset[]) => {
      this.assets = data;
      this.filteredAssets = data;
      this.cdr.detectChanges();
    });
  }

  addAsset(addAssetForm: NgForm) {
    this.assetService.addAsset(this.assetData)
      .subscribe(
        (response) => {
          console.log('Asset type Add successful:', response);
          this.loadAsset();
          addAssetForm.resetForm();
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
  
  editAsset(asset: Asset) {
    this.selectedAsset = { ...asset };
    this.originalAsset = { ...asset };
  }

  updateAsset(updatedAsset: Asset) {
    const updateRequest = {
      user: updatedAsset.user,
      status: updatedAsset.assetStatus
    };
    this.assetService.updateAsset(updatedAsset.assetId, updateRequest).subscribe(
      (response: any) => {
        console.log('Asset updated successfully:', response);
        const index = this.assets.findIndex(asset => asset.assetId === updatedAsset.assetId);
        if (index !== -1) {
          this.assets[index] = {
            ...this.assets[index],
            assetStatus: updatedAsset.assetStatus,
            user: updatedAsset.user !== null ? updatedAsset.user : this.assets[index].user
          };
        }
        this.selectedAsset = null;
      },
      (error) => {
        console.error('Asset update failed:', error);
      }
    );
  }
  
  cancelEdit() {
    if (this.originalAsset) {
      this.selectedAsset = { ...this.originalAsset };
      this.originalAsset = null;
    } else {
      this.selectedAsset = null;
    }
  }


}
