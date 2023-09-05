import { ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { UserAuthService } from '../_services/user-auth.service';
import { Router } from '@angular/router';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{
  userData: any;
  isAuthenticated: boolean;

  constructor(
    private userAuthService: UserAuthService,
    private router: Router,
    public userService: UserService,
    private cdr: ChangeDetectorRef
 
  ) { 
    this.isAuthenticated = !!localStorage.getItem('jwtToken');
  }

  ngOnInit(): void {
      
  }

  @HostListener('window:storage', ['$event'])
  onStorageChange(event: StorageEvent) {
    // log out if local storage is cleared
    if (!event.key) {
      this.isAuthenticated = false;
      this.cdr.detectChanges();
      return
    }

    if (event.key === 'jwtToken' && event.newValue) {
      this.isAuthenticated = true;
      this.cdr.detectChanges();
      return
    }
    
    if (event.key === 'jwtToken' && !event.newValue) {
      this.isAuthenticated = false;
      this.cdr.detectChanges();
      return
    }
  }

  public isLoggedIn(): boolean {
    return this.userAuthService.isLoggedIn();
  }

  public logout() {
    console.log('Logout function called');
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('roles');
    this.userAuthService.clear();
    this.router.navigate(['/home']);
  }


}
