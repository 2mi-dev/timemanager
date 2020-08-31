import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PasswordService {

  constructor(private http: HttpClient) {}

  save(oldPassword: string, newPassword: string): Observable<any> {
    return this.http.post('api/account/change-password', { oldPassword, newPassword });
  }
}
