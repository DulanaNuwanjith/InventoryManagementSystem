import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AssetTypeService {

  backendUrl = 'http://localhost:8087/api/asset-types'; 

  constructor(private httpclient: HttpClient) { }

  getAllAssetTypes(): Observable<any[]> {
    return this.httpclient.get<any[]>(this.backendUrl + "/all");
  }

  addAssetType(assetTypeData: any) {
    return this.httpclient.post(this.backendUrl + "/add", assetTypeData)
  }

  searchAssetTypes(searchText: string): Observable<any[]> {
    const apiUrl = `your-api-endpoint?search=${searchText}`;
    return this.httpclient.get<any[]>(this.backendUrl + "/all");
  }

  deleteAssetTypeByTypeId(typeId: number): Observable<any> {
    return this.httpclient.delete(`${this.backendUrl}/${typeId}`);
  }
}
