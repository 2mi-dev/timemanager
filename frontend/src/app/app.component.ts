import { Component, HostBinding } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { SettingsService } from './core/settings/settings.service';
import { filter } from 'rxjs/operators';
import { TitleService } from './core/title/title.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent {
  @HostBinding('class.layout-fixed')
  get isFixed() {
    return this.settings.layout.fixed;
  }
  @HostBinding('class.layout-boxed')
  get isBoxed() {
    return this.settings.layout.boxed;
  }
  @HostBinding('class.aside-collapsed')
  get isCollapsed() {
    return this.settings.layout.collapsed;
  }

  constructor(
    private router: Router,
    private settings: SettingsService,
    private titleService: TitleService
  ) {}

  ngOnInit() {

    this.router.events
    .pipe(filter(evt => evt instanceof NavigationEnd))
    .subscribe(() => this.titleService.setTitle());
  }
}
