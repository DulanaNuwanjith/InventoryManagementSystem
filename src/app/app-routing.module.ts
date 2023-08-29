import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { UserRegistrationComponent } from './user-registration/user-registration.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { AssetTypeManagementComponent } from './asset-type-management/asset-type-management.component';
import { AssetManagementComponent } from './asset-management/asset-management.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { UserProfileManagementComponent } from './user-profile-management/user-profile-management.component';
import { UserChangePasswordComponent } from './user-change-password/user-change-password.component';
import { UserViewAssetComponent } from './user-view-asset/user-view-asset.component';

const routes: Routes = [
  {
    path: '',
    component: HomePageComponent
  },
  {
    path:'registration',
    component: UserRegistrationComponent
  },
  {
    path: 'login',
    component: UserLoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {
    path: 'user-management',
    component: UserManagementComponent
  },
  {
    path: 'asset-type-management',
    component: AssetTypeManagementComponent
  },
  {
    path: 'asset-management',
    component: AssetManagementComponent
  },
  {
    path: 'user-dashboard',
    component: UserDashboardComponent
  },
  {
    path: 'user-profile-management',
    component: UserProfileManagementComponent
  },
  {
    path: 'user-change-password',
    component: UserChangePasswordComponent
  },
  {
    path: 'user-view-asset',
    component: UserViewAssetComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
