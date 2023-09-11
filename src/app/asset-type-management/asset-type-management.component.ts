import { Component, OnInit } from '@angular/core';
import { AssetTypeService } from '../_services/asset-type.service';
import { NgForm } from '@angular/forms';
import { Asset } from '../_model/asset.model';
import { MatDialog } from '@angular/material/dialog';
import { AssetPopupComponent } from '../asset-popup/asset-popup.component';
import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

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
  typeName: string = '';

  assetTypeData = {
    typeName: '',
  };

  constructor(private assetTypeService: AssetTypeService,
    public dialog: MatDialog, private snackBar: MatSnackBar) { }

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
    if (addAssetTypeForm.invalid) {
      return;
    }
  
    this.assetTypeService.addAssetType(this.assetTypeData).subscribe(
      (response) => {
        console.log('Asset type Add successful:', response);
        addAssetTypeForm.resetForm();
        this.loadAssetType();
  
        this.snackBar.open('Asset type added successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: 'success-snackbar',
        });
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

  viewAssetsByTypeName(typeName: string) {
    console.log(this.filteredAssetTypes)
    console.log({typeName})
    this.assetTypeService.getAssetsByAssetTypeName(typeName).subscribe(
      (assets: Asset[]) => {
        console.log({assets})
        const dialogRef = this.dialog.open(AssetPopupComponent, {
          data: { typeName, assets },
        });
      },
      (error) => {
        console.error('Error fetching assets:', error);
      }
    );
  }
  
  openDeleteConfirmationDialog(typeId: number) {
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
        data: typeId,
    });

    dialogRef.afterClosed().subscribe((result) => {
        if (result === true) {
            this.deleteAssetTypeByTypeId(typeId);
        }
    });
}

}
