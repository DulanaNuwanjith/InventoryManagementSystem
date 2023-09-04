import { Component, OnInit } from '@angular/core';
import { UserAuthService } from '../_services/user-auth.service';
import { Router } from '@angular/router';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {
  userData: any;

  constructor(
    private userAuthService: UserAuthService,
    private router: Router,
    public userService: UserService
  ) { }

  isLoggedIn(): boolean {
    return true;
  }
  
  public logout() {
    console.log('Logout function called');
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('roles');
    this.userAuthService.clear();
    this.router.navigate(['/home']);
}
  
  
}
