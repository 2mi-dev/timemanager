import { APP_INITIALIZER, InjectionToken, NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { Ng2Webstorage } from 'ngx-webstorage';

import { AuthInterceptor } from './auth/auth.interceptor';
import { StartupService } from './startup/startup.service';

export function StartupServiceFactory(startupService: StartupService): Function {
  return () => startupService.load();
}

export const WINDOW = new InjectionToken('Window');

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    RouterModule,
    Ng2Webstorage.forRoot({ prefix: 'app', separator: '-' })
  ],
  declarations: [

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    StartupService,
    {
      provide: APP_INITIALIZER,
      useFactory: StartupServiceFactory,
      deps: [StartupService],
      multi: true
    },
    { provide: WINDOW, useValue: window }
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded. Import it in the AppModule only');
    }
  }
}
