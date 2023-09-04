import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { UserRegistrationComponent } from './user-registration/user-registration.component';
import { HomePageComponent } from './home-page/home-page.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { AssetTypeManagementComponent } from './asset-type-management/asset-type-management.component';
import { AssetManagementComponent } from './asset-management/asset-management.component';
import { UserProfileManagementComponent } from './user-profile-management/user-profile-management.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserChangePasswordComponent } from './user-change-password/user-change-password.component';
import { UserViewAssetComponent } from './user-view-asset/user-view-asset.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent,
    UserRegistrationComponent,
    HomePageComponent,
    DashboardComponent,
    UserManagementComponent,
    AssetTypeManagementComponent,
    AssetManagementComponent,
    UserProfileManagementComponent,
    UserDashboardComponent,
    UserChangePasswordComponent,
    UserViewAssetComponent,
    NavBarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
