import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd';

import { AuthService } from '../core/auth/auth.service';
import { User } from '../core/user/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.less']
})
export class ProfileComponent implements OnInit {
  form: FormGroup;
  loading = false;
  submitting = false;
  failed = false;

  user: User;

  constructor(private fb: FormBuilder,
              private msg: NzMessageService,
              private authService: AuthService
  ) {
  }

  ngOnInit(): void {

    this.form = this.fb.group({
      username: [
        null,
        [
          Validators.required
        ]
      ],
      nickname: [
        null,
        [
          Validators.maxLength(20)
        ]
      ],
      name: [
        null,
        [
          Validators.maxLength(20)
        ]
      ],
      email: [
        null,
        [
          Validators.email,
          Validators.maxLength(100)
        ]
      ],
      phone: [
        null,
        [
          Validators.maxLength(20)
        ]
      ]
    });
    this.loading = true;
    this.authService.getPrincipal().subscribe(principal => {
        this.user = Object.assign({}, principal);
        this.form.patchValue(this.user);
        this.loading = false;
      },
      (error) => {
        this.msg.error('Failed to load personal information');
        this.loading = false;
      }
    );
  }


  get username() {
    return this.form.controls.username;
  }

  get nickname() {
    return this.form.controls.nickname;
  }

  get name() {
    return this.form.controls.name;
  }

  get email() {
    return this.form.controls.email;
  }

  get phone() {
    return this.form.controls.phone;
  }

  submit() {
    for (const i in this.form.controls) {
      this.form.controls[i].markAsDirty();
      this.form.controls[i].updateValueAndValidity();
    }
    if (this.form.valid) {
      this.submitting = true;
      this.failed = false;
      this.authService.updateAccount(this.form.value).subscribe(
        () => {
          this.submitting = false;
          this.msg.success(`Personal information update successfully!`);
          this.authService.getPrincipal(true).subscribe(principal => {
            this.user = Object.assign({}, principal);
            this.form.patchValue(this.user);
          });
        },
        () => {
          this.submitting = false;
          this.failed = true;
          this.msg.error(`Personal information update failed!`);
        }
      );
    }
  }
}

