import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Asset } from '../_model/asset.model';

@Component({
  selector: 'app-asset-popup',
  templateUrl: './asset-popup.component.html',
  styleUrls: ['./asset-popup.component.css']
})
export class AssetPopupComponent {
  typeName: string | undefined;
  assets: Asset[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    if (data && data.typeName) {
      this.typeName = data.typeName;
      this.assets = data.assets || [];
    }
    console.log('ppopup');
    
  }

}
