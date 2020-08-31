import { Injectable } from '@angular/core';
import { Router, Route, CanActivate, CanActivateChild, CanLoad, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import { AuthService } from './auth.service';
import { StateStorageService } from './state-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild, CanLoad {

  constructor(private router: Router,
              private authService: AuthService,
              private stateStorageService: StateStorageService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    const authorities = next.data['authorities'];
    return this.checkAuth(authorities, state.url);
  }

  canActivateChild(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.canActivate(next, state);
  }

  canLoad(route: Route): Observable<boolean> | Promise<boolean> | boolean {
    const authorities = route.data['authorities'];
    const url = `/${route.path}`;
    return this.checkAuth(authorities, url);
  }

  checkAuth(authorities: string[], url: string): Observable<boolean> | Promise<boolean> | boolean {
    const authService = this.authService;
    return authService.getPrincipal().toPromise().then(
      (account) => {
        if (!authorities || authorities.length === 0) {
          return true;
        }
        if (account) {
          const authed: boolean = authService.hasAnyAuthorityDirect(authorities);
          if (!authed) {
            this.router.navigate(['forbidden']);
          }
          return authed;
        }
      }
    ).catch(
      (err) => {
        this.router.navigate(['login']);
        return false;
      }
    );
  }
}

