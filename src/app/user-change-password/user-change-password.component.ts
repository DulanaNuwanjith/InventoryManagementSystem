import { Component } from '@angular/core';
import { UserAuthService } from '../_services/user-auth.service';

@Component({
  selector: 'app-user-change-password',
  templateUrl: './user-change-password.component.html',
  styleUrls: ['./user-change-password.component.css']
})
export class UserChangePasswordComponent {

  oldPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private authService: UserAuthService) {}

  onSubmit() {
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'New and confirm passwords do not match.';
      return;
    }

    this.authService.changePassword(this.oldPassword, this.newPassword)
      .subscribe(
        (response) => {
          this.successMessage = response.message;
          this.errorMessage = '';
          this.clearForm();
        },
        (error) => {
          this.successMessage = '';
          this.errorMessage = error.error.message;
        }
      );
  }

  clearForm() {
    this.oldPassword = '';
    this.newPassword = '';
    this.confirmPassword = '';
  }

}
