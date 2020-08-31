import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NavigationEnd, NavigationError, RouteConfigLoadStart, Router } from '@angular/router';
import { ScrollService } from '../core/scroll/scroll.service';
import { NzMessageService } from 'ng-zorro-antd';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.less'],
  encapsulation: ViewEncapsulation.None
})
export class LayoutComponent implements OnInit {

  isFetching = false;

  constructor(router: Router,
              scroll: ScrollService,
              private messageService: NzMessageService) {
    router.events.subscribe(evt => {
      if (!this.isFetching && evt instanceof RouteConfigLoadStart) {
        this.isFetching = true;
      }
      if (evt instanceof NavigationError) {
        this.isFetching = false;
        messageService.error(`Cannot load ${evt.url} route`, {nzDuration: 1000 * 3 });
        return;
      }
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      setTimeout(() => {
        scroll.scrollToTop();
        this.isFetching = false;
      }, 100);
    });
  }

  ngOnInit() {
  }
}
