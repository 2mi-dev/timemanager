import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../../../core/user/user';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { Subscription } from 'rxjs';

// const colorList = ['#f56a00', '#7265e6', '#ffbf00', '#00a2ae'];


@Component({
  selector: 'app-header-user',
  templateUrl: './header-user.component.html',
  styleUrls: ['./header-user.component.less']
})
export class HeaderUserComponent implements OnInit, OnDestroy {

  principal: User;

  private principalChange: Subscription;

  // color: string = colorList[0];

  constructor(private router: Router,
              private authService: AuthService) {
    this.principalChange = authService.authenticationState.subscribe((principal) => {
      this.principal = principal;
    });
  }

  ngOnInit() {
    this.authService.getPrincipal().subscribe(
      (principal) => {
        this.principal = principal;
        // const lang = principal.langKey;
        // if (!!lang) {
        //   // lang = lang.replace(/_/g, '-');
        //   if (!this.settings.layout.lang) {
        //     this.i18nService.use(lang, false).subscribe(() => {
        //       this.menuService.resume();
        //       // this.reuseTabService.clear();
        //     });
        //     this.settings.setLayout('lang', lang);
        //   }
        // }
      }
    );
  }

  get username(): String {
    if (this.principal) {
      return this.principal.nickname || this.principal.name || this.principal.username;
    } else {
      return '';
    }
  }

  // get avatarChar(): String {
  //   if (this.principal) {
  //     const name = this.principal.nickname || this.principal.name || this.principal.username;
  //     return name.charAt(0).toUpperCase();
  //   } else {
  //     return '';
  //   }
  // }

  logout() {
    this.authService.logout().subscribe();
    this.router.navigate(['login']);
  }

  ngOnDestroy(): void {
    if (this.principalChange) {
      this.principalChange.unsubscribe();
    }
  }

}
