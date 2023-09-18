import { Component } from '@angular/core';
import { Asset } from '../_model/asset.model';
import { AssetService } from '../_services/asset.service';

@Component({
  selector: 'app-available-assets-list',
  templateUrl: './available-assets-list.component.html',
  styleUrls: ['./available-assets-list.component.css']
})
export class AvailableAssetsListComponent {

  assets: Asset[] = [];

  constructor(private assetService: AssetService) {}

  ngOnInit() {
    this.loadAsset();
  }

  loadAsset() {
    this.assetService.getAllAssets().subscribe((data: Asset[]) => {
      this.assets = data;
    });
  }

}
