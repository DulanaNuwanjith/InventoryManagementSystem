import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Asset } from '../_model/asset.model';

@Injectable({
  providedIn: 'root'
})
export class AssetService {

  backendUrl = 'http://localhost:8087/api/assets';

  constructor(private httpclient: HttpClient) { }

  getAllAssets(): Observable<any[]> {
    return this.httpclient.get<any[]>(this.backendUrl + "/all");
  }

  addAsset(assetData: any) {
    return this.httpclient.post(this.backendUrl + "/add", assetData)
  }

  deleteAssetById(assetId: number): Observable<any> {
    return this.httpclient.delete(`${this.backendUrl}/${assetId}`);
  }

  searchAsset(searchText: string): Observable<any[]> {
    return this.httpclient.get<any[]>(this.backendUrl + "/all");
  }

  getUserAssets(): Observable<any[]> {
    return this.httpclient.get<any[]>(`${this.backendUrl}/my/assets`);
  }

  updateAsset(assetId: number, updatedAsset: any): Observable<any> {
    return this.httpclient.put(`${this.backendUrl}/${assetId}`, updatedAsset);
  }

}
