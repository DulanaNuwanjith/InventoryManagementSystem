import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-user-profile-management',
  templateUrl: './user-profile-management.component.html',
  styleUrls: ['./user-profile-management.component.css']
})
export class UserProfileManagementComponent implements OnInit {

  profileForm: FormGroup;
  userDetails: any = {};

  constructor(private userService: UserService, private formBuilder: FormBuilder) {
    this.profileForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phoneno: [''],
    });
  }

  ngOnInit() {
    this.loadUserDetails();
  }

  loadUserDetails() {
    this.userService.getCurrentUserDetails().subscribe(
      (data: any) => {
        this.userDetails = data;
        this.profileForm.patchValue({
          firstName: this.userDetails.firstName,
          lastName: this.userDetails.lastName,
          phoneno: this.userDetails.phoneno
        });
      },
      (error) => {
        console.error('Error fetching user details:', error);
      }
    );
  }

  updateProfile() {
    if (this.profileForm.valid) {
      const updatedData = this.profileForm.value;

      this.userService.updateUserProfile(updatedData).subscribe(
        (response: any) => {
          console.log('Profile updated successfully.');
          this.loadUserDetails();
        },
        (error) => {
          console.error('Error updating user profile:', error);
        }
      );
    } else {}
  }
  
}
