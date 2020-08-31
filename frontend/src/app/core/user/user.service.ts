import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { User } from './user';
import { UserCriteria } from './user-criteria';
import { Page } from '../interface/page';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  private resourceUrl = `api/users`;

  constructor(private http: HttpClient) { }

  validateUsernameNotTaken(username: String, id?: number): Observable<any> {
    return this.http.post(`api/validators/username-not-taken`, { value: username, id: id });
  }

  get(id: number): Observable<User> {
    return this.http.get(`${this.resourceUrl}/${id}`);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  create(user: User): Observable<User> {
    return this.http.post(this.resourceUrl, user);
  }

  update(user: User): Observable<User> {
    return this.http.put(this.resourceUrl, user);
  }

  query(criteria?: UserCriteria): Observable<Page<User>> {
    let params = new HttpParams();
    if (criteria) {
      params = params.append('page', String(criteria.page));
      params = params.append('size', String(criteria.size));
      if (criteria.sort) {
        params = params.append('sort', criteria.sort);
      }
      if (criteria.filter) {
        params = params.append('filter', criteria.filter);
      }
      if (criteria.username) {
        params = params.append('username', criteria.username);
      }
      if (criteria.nickname) {
        params = params.append('nickname', criteria.nickname);
      }
      if (criteria.name) {
        params = params.append('name', criteria.name);
      }
      if (criteria.email) {
        params = params.append('email', criteria.email);
      }
      if (criteria.enabled != null) {
        params = params.append('enabled', String(criteria.enabled));
      }
      if (criteria.activated != null) {
        params = params.append('activated', String(criteria.activated));
      }
      if (criteria.authority) {
        params = params.append('authority', criteria.authority);
      }
    }
    console.log(params.toString());
    return this.http.get<Page<User>>(this.resourceUrl, { params: params });
  }
}
