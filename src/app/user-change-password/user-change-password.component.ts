import { Component } from '@angular/core';
import { UserAuthService } from '../_services/user-auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

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

  constructor(private authService: UserAuthService, private snackBar: MatSnackBar) { }

  onSubmit() {
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'New and confirm passwords do not match.';
      return;
    }

    this.authService.changePassword(this.oldPassword, this.newPassword)
      .subscribe(
        (response) => {
          this.errorMessage = '';
          this.clearForm();

          this.snackBar.open('Successfully updated password', 'Close', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['success-snackbar'],
          });
          this.successMessage = 'Password successfully updated.';
        },
        (error) => {
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
