import { Component, OnInit } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { UserService } from '../_services/user.service';
import { UserAuthService } from '../_services/user-auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})

export class UserLoginComponent implements OnInit {
  constructor(
    private userService: UserService,
    private userAuthService: UserAuthService,
    private router: Router,
  ) {

  }

  ngOnInit(): void {

  }

  login(loginForm: NgForm) {
    this.userService.login(loginForm.value).subscribe(
      (response: any) => {
  
        if (response && response.roles) {
          this.userAuthService.setRoles(response.roles);
          this.userAuthService.setToken(response.token);
          this.userAuthService.login();
  
          const roles = response.roles[0]; 
          if (roles === 'ROLE_ADMIN') {
            this.router.navigate(['/dashboard']);

          } else {
            this.router.navigate(['/user-dashboard']);
          }
        } else {
          console.log("Response structure is invalid. Missing 'roles'.");

        }
      },
      (error) => {
        console.log(error);

      }
    );
  }

}

