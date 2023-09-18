import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AssetTypeService {

  baseUrl
  Url
  private domian: string | undefined;

  constructor(private httpclient: HttpClient) {
    this.baseUrl = environment.domain + "asset-types";
    this.Url = environment.domain + "assets";
   }

  getAllAssetTypes(): Observable<any[]> {
    return this.httpclient.get<any[]>(this.baseUrl + "/all");
  }

  addAssetType(assetTypeData: any) {
    return this.httpclient.post(this.baseUrl + "/add", assetTypeData)
  }

  searchAssetTypes(searchText: string): Observable<any[]> {
    return this.httpclient.get<any[]>(this.baseUrl + "/all");
  }

  deleteAssetTypeByTypeId(typeId: number): Observable<any> {
    return this.httpclient.delete(`${this.baseUrl}/${typeId}`);
  }

  getAssetsByAssetTypeName(typeName: string): Observable<any[]> {
    return this.httpclient.get<any[]>(`${this.Url}/byAssetTypeName/${typeName}`);
  }
  
}
