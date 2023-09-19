import { Component, OnInit } from '@angular/core';
import { UserAuthService } from '../_services/user-auth.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  isAuthenticated: boolean = false;

  constructor(private userAuthService: UserAuthService) { }

  ngOnInit(): void {
    this.isAuthenticated = this.userAuthService.isLoggedIn();
  }

}
