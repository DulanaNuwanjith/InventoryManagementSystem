import { Component, OnInit } from '@angular/core';
import { AssetService } from '../_services/asset.service';
import { Asset } from '../_model/asset.model';

@Component({
  selector: 'app-user-view-asset',
  templateUrl: './user-view-asset.component.html',
  styleUrls: ['./user-view-asset.component.css']
})
export class UserViewAssetComponent implements OnInit{

  assets: Asset[] = [];

    constructor(private assetService: AssetService) {}

    ngOnInit() {
        this.assetService.getUserAssets().subscribe((data: any[]) => {
            this.assets = data;
        });
    }

}
