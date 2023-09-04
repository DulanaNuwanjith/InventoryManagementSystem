import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserAuthService } from './user-auth.service';

interface LoginData {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  PATH_OF_API = "http://localhost:8087/api/auth";
  requestHeader = new HttpHeaders(
    {
      "No-Auth": "True"
    }
  )

  constructor(
    private httpclient: HttpClient,
    private userAuthService: UserAuthService
  ) { }

  public login(loginData: LoginData) {
    return this.httpclient.post(this.PATH_OF_API + "/signin", loginData, { headers: this.requestHeader })
  }

  public roleMatch(allowedRoles: string[]): boolean {
    const userRoles: string[] | null = this.userAuthService.getRoles();
  
    if (userRoles !== null) {
      for (let i = 0; i < userRoles.length; i++) {
        for (let j = 0; j < allowedRoles.length; j++) {
          if (userRoles[i] === allowedRoles[j]) {
            return true;
          }
        }
      }
    }
  
    return false;
  }
  
}