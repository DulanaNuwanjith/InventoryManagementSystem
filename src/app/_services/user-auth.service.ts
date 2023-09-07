import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {
  isAuthenticated = new Subject<boolean>();
  constructor() {
  }

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

  public isLoggedIn(): boolean {
    return !!this.getRoles() && !!this.getToken();
  }  

  // private isAuthenticated = false;

  logout() {
    this.isAuthenticated.next(false);
    localStorage.clear(); 
  }

  public login() {
    // Perform your login logic here
    // Set isAuthenticated to true upon successful login
    this.isAuthenticated.next(true);
  }

  public getAuthState(): Observable<boolean> {
    return this.isAuthenticated.asObservable();
  }

  
}
