import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder,FormGroup,Validators} from '@angular/forms';

import { AuthService } from '../core/auth/auth.service';
import { StateStorageService } from '../core/auth/state-storage.service';
import { SettingsService } from '../core/settings/settings.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  failed = false;
  submitting = false;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private stateStorageService: StateStorageService,
              public settings: SettingsService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: [ null, [ Validators.required ] ],
      password: [ null, [ Validators.required ] ],
      rememberMe: [ true ]
    });
  }


  get username() { return this.form.controls.username; }

  get password() { return this.form.controls.password; }

  submitForm(): void {
    for (const i in this.form.controls) {
      this.form.controls[ i ].markAsDirty();
      this.form.controls[ i ].updateValueAndValidity();
    }
    if (this.form.valid) {
      this.submitting = true;
      this.failed = false;
      this.authService.login(this.form.value).subscribe(
        (data) => {
          this.submitting = false;
          const redirect = this.stateStorageService.getUrl();
          if (redirect) {
            this.stateStorageService.storeUrl(null);
            this.router.navigate([redirect]);
          } else {
            this.router.navigate(['']);
          }
          //this.menuService.resume();

        },
        (error) => {
          this.submitting = false;
          this.failed = true;
        }
      );
    }
  }
}
