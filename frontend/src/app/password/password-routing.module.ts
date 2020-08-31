import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { PasswordComponent } from './password.component';


const routes: Routes = [
  {
    path: '',
    component: PasswordComponent,
    canActivate: [AuthGuard],
    data: { authorities: ['ROLE_USER'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PasswordRoutingModule { }
