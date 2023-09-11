import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AssetService } from '../_services/asset.service';
import { Asset } from '../_model/asset.model';
import { NgForm } from '@angular/forms';
import { AssetTypeService } from '../_services/asset-type.service';
import { UserService } from '../_services/user.service';
import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';


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
  assetTypes: any[] = [];
  users: any[] = [];

  assetData = {
    assetName: '',
    typeName: '',
  };

  constructor(private assetService: AssetService, private cdr: ChangeDetectorRef, private assetTypeService: AssetTypeService, private userService: UserService, private dialog: MatDialog, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadAsset();
    this.loadAssetTypes();
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers().subscribe(
      (data: any[]) => {
        console.log('Response from getUsers API:', data);
        this.users = data;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );
  }


  loadAsset() {
    this.assetService.getAllAssets().subscribe((data: Asset[]) => {
      this.assets = data;
      this.filteredAssets = data;
      this.cdr.detectChanges();
    });
  }

  addAsset(addAssetForm: NgForm) {
    this.assetService.addAsset(this.assetData).subscribe(
      (response) => {
        console.log('Asset type Add successful:', response);
        this.loadAsset();
        addAssetForm.resetForm();
        
        this.snackBar.open('Asset added successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar'],
        });
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
        this.snackBar.open('Asset updated successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar'],
        });
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

  loadAssetTypes() {
    this.assetTypeService.getAllAssetTypes().subscribe(
      (data: any[]) => {
        this.assetTypes = data;
      },
      (error) => {
        console.error('Error fetching asset types:', error);
      }
    );
  }

  toggleDropdown(event: MouseEvent) {
    const selectElement = event.target as HTMLSelectElement;
    selectElement.focus();
  }

  openDeleteConfirmationDialog(asset: Asset) {
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
      data: asset,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.deleteAssetById(asset.assetId);
      }
    });
  }

}
