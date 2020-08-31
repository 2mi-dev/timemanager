import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';


import { UserComponent } from './user.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserEditComponent } from './user-edit/user-edit.component';
import { AuthGuard } from '../core/auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: UserComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        component: UserListComponent,
        data: {
          authorities: ['ROLE_ADMIN']
        }
      },
      {
        path: ':id',
        component: UserEditComponent,
        data:
          {
            title: '用户编辑',
            authorities: ['ROLE_ADMIN']
          }
      },
    ]

  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule {
}
