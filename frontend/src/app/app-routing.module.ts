import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './core/auth/auth.guard';
//
import { LayoutComponent } from './layout/layout.component';
import { ForbiddenComponent } from './page/forbidden/forbidden.component';
import { NotFoundComponent } from './page/not-found/not-found.component';
import { ErrorComponent } from './page/error/error.component';
import { LoginComponent } from './login/login.component';
import { SelectivePreloadingStrategy } from './core/preloading-strategy/selective-preloading-strategy';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: '登录'
    }
  },
  {
    path: 'forbidden',
    component: ForbiddenComponent,
    data: {
      title: '无权访问'
    }
  },
  {
    path: 'error',
    component: ErrorComponent,
    data: {
      title: '服务器出错'
    }
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'dashboard',
        loadChildren: './dashboard/dashboard.module#DashboardModule',
        canLoad: [AuthGuard],
        data: {
          preload: true,
          authorities: ['ROLE_USER']
        }
      },
      {
        path: 'password',
        loadChildren: './password/password.module#PasswordModule',
        canLoad: [AuthGuard],
        data: {
          authorities: ['ROLE_USER']
        }
      },
      {
        path: 'profile',
        loadChildren: './profile/profile.module#ProfileModule',
        canLoad: [AuthGuard],
        data: {
          authorities: ['ROLE_USER']
        }
      },
      {
        path: 'users',
        loadChildren: './user/user.module#UserModule',
        canLoad: [AuthGuard],
        data: {
          authorities: ['ROLE_ADMIN']
        }
      },
    ],
    canActivateChild: [AuthGuard],
    data: {
      authorities: ['ROLE_USER']
    }
  },
  {
    path: '**',
    component: NotFoundComponent,
    data: {
      title: '页面不存在'
    }
  }

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
      {
        //enableTracing: true, // <-- debugging purposes only
        preloadingStrategy: SelectivePreloadingStrategy
      })
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
