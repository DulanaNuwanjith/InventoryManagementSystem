import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: any[] = [];
  searchText: string = '';
  suggestions: string[] = [];

  constructor(private userService: UserService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.userService.getUsers().subscribe((data) => {
      this.users = data;
    });
  }

  loadUsers() {
    this.userService.getUsers().subscribe((data) => {
      this.users = data;
    });
  }

  deleteUser(userId: number) {
    const newState = 'INACTIVE';
    this.userService.updateUserState(userId, newState).subscribe(() => {
      this.loadUsers();
    });
  }

  searchUsers() {
    if (this.searchText.trim() === '') {
      this.loadUsers();
    } else {
      const searchTerms = this.searchText.trim().toLowerCase().split(' ');

      this.users = this.users.filter((user) => {
        return searchTerms.some((term) => {
          if (user.id.toString().toLowerCase().includes(term)) {
            return true;
          }

          return Object.values(user).some((value) => {
            if (typeof value === 'string') {
              return value.toLowerCase().includes(term);
            }
            return false;
          });
        });
      });
    }
  }


  selectSuggestion(suggestion: string) {
    this.searchText = suggestion;
    this.searchUsers();
    this.suggestions = [];
  }

  showSuggestions(input: string) {
    this.suggestions = [];

    for (let i = 1; i <= input.length; i++) {
      const suggestion = input.slice(0, i);
      this.suggestions.push(`Suggestion for ${suggestion}`);
    }
  }
  openDeleteConfirmationDialog(userId: number) {
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
      data: userId,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.deleteUser(userId);
      }
    });
  }

  getRoleDisplayName(userRoles: string): string {
    if (userRoles.includes('ROLE_USER')) {
      return 'User';
    } else if (userRoles.includes('ROLE_ADMIN')) {
      return 'Admin';
    } else {
      return 'Other';
    }
  }


}


// assets: Asset[] = [];

// deleteUser(userId: number) {
//   const newState = 'INACTIVE';
//   this.userService.updateUserState(userId, newState).subscribe(() => {
//     this.assetService.getAssetsByUserId(userId).subscribe((assets: Asset[]) => {
//       for (const asset of assets) {
//         asset.assetStatus = 'AVAILABLE';
//         this.assetService.updateAsset(asset.assetId, { assetStatus: 'AVAILABLE' }).subscribe(() => {
//         }, (error) => {
//           console.error('Error updating asset status:', error);
//         });
//       }
//       this.loadAssets();
//     });
//     this.loadUsers();
//   });
// }

// loadAssets() {
//   this.assetService.getAllAssets().subscribe((assets: Asset[]) => {
//     this.assets = assets;
//   });
// }