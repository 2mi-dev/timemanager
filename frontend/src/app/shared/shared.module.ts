import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgZorroAntdModule } from 'ng-zorro-antd';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { ViserModule } from 'viser-ng';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    ViserModule,
    NgZorroAntdModule

  ],
  declarations: [
    HasAnyAuthorityDirective
  ],
  exports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    NgZorroAntdModule,
    ViserModule,
    HasAnyAuthorityDirective
  ],
  providers: [
    HasAnyAuthorityDirective
  ]

})
export class SharedModule { }
