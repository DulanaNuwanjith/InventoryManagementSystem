import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  constructor() { }

  public setRoles(roles:[]){
    localStorage.setItem('roles', JSON.stringify(roles));
  }

  public getRoles(): string[] {
    const rolesJson = localStorage.getItem('roles');
    return rolesJson ? JSON.parse(rolesJson) : [];
  }

  public setToken(jwtToken:string){
    localStorage.setItem("jwtToken",jwtToken)
  }

  public getToken(): string {
    return localStorage.getItem('jwtToken') as string || '';
  }

  public clear(){
    localStorage.clear();
  }

  public isLoggedIn(){
    this.getRoles() && this.getToken();
  }
  
}