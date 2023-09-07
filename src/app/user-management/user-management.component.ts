import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: any[] = [];
  searchText: string = '';
  suggestions: string[] = [];

  constructor(private userService: UserService) { }

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
    if (confirm('Are you sure you want to delete this user?')) {
      const newState = 'INACTIVE';
      this.userService.updateUserState(userId, newState).subscribe(() => {
        this.loadUsers();
      });
    }
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
  
  

}
