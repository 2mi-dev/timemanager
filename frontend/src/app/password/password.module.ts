import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared.module';

import { PasswordRoutingModule } from './password-routing.module';
import { PasswordComponent } from './password.component';


@NgModule({
  imports: [
    SharedModule,
    PasswordRoutingModule
  ],
  declarations: [PasswordComponent]
})
export class PasswordModule { }
