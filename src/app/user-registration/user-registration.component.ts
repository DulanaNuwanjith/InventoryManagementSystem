import { Component } from '@angular/core';
import { UserService } from '../_services/user.service';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent {

  userData = {
    firstName: '',
    lastName: '',
    phoneno: '',
    username: '',
    email: '',
    password: '',
  };

  constructor(private userService: UserService, private snackBar: MatSnackBar) { }

  registerUser(registrationForm: NgForm) {
    this.userService.register(this.userData)
      .subscribe(
        (response) => {
          console.log('Registration successful:', response);
          registrationForm.resetForm();

          this.snackBar.open('Registration successful', 'Close', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
        },
        (error) => {
          console.error('Registration failed:', error);

          this.snackBar.open('Registration failed', 'Close', {
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: 'error-snackbar',
          });
        }
      );
  }

}
