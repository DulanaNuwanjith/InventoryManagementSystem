import { Component } from '@angular/core';
import { UserService } from '../_services/user.service';
import { NgForm } from '@angular/forms';

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

  constructor(private userService: UserService) {}

  registerUser(registrationForm: NgForm) {
    this.userService.register(this.userData)
      .subscribe(
        (response) => {
          console.log('Registration successful:', response);
          registrationForm.resetForm();
        },
        (error) => {
          console.error('Registration failed:', error);
        }
      );
  }

}
