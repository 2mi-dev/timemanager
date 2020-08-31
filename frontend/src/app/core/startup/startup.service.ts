import { Injectable, Injector } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { zip } from 'rxjs';

import { SettingsService } from '../settings/settings.service';
import { TitleService } from '../title/title.service';
import { catchError } from 'rxjs/operators';


@Injectable()
export class StartupService {

  constructor(private settingService: SettingsService,
              private titleService: TitleService,
              private httpClient: HttpClient,
              private injector: Injector) { }


  load(): Promise<any> {
    // only works with promises
    // https://github.com/angular/angular/issues/15088
    return new Promise((resolve, reject) => {
      zip(
        this.httpClient.get(`actuator/info`)
      )
      .pipe(
        // 接收其他拦截器后产生的异常消息
        catchError(([appData]) => {
          resolve(null);
          return [appData];
        }),
      )
      .subscribe(
        ([appData]) => {
          console.log(appData);

          // application data
          const res: any = appData;
          // 应用信息：包括站点名、描述、年份
          this.settingService.setApp(res.build);
          // 设置页面标题的后缀
          this.titleService.suffix = res.build.name;
        },
        () => {},
        () => {
          resolve(null);
        },
      );
    });
  }
}
