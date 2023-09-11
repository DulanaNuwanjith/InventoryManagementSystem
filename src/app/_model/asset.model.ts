import { AssetType } from "./asset-type.model";

export class Asset {
    assetId: number;
    assetName: string;
    assetStatus: string;
    assetType: AssetType
    user: number;
  
    constructor(assetId: number, assetName: string, assetStatus: string, assetType: AssetType, user: number, ) {
      this.assetId = assetId;
      this.assetName = assetName;
      this.assetStatus = assetStatus;
      this.assetType = assetType;
      this.user = user;
    }
  }
  