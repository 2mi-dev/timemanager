import { Injectable } from '@angular/core';
import { App } from '../interface/app';
import { Layout } from '../interface/layout';


const LAYOUT_KEY = 'layout';
const APP_KEY = 'app';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private _app: App = null;
  private _layout: Layout = null;

  private get(key: string) {
    return JSON.parse(localStorage.getItem(key) || 'null') || null;
  }

  private set(key: string, value: any) {
    localStorage.setItem(key, JSON.stringify(value));
  }

  get layout(): Layout {
    if (!this._layout) {
      this._layout = Object.assign(
        <Layout>{
          fixed: true,
          collapsed: false,
          boxed: false,
          lang: null,
        },
        this.get(LAYOUT_KEY),
      );
      this.set(LAYOUT_KEY, this._layout);
    }
    return this._layout;
  }

  get app(): App {
    if (!this._app) {
      this._app = Object.assign(
        <App>{
          year: new Date().getFullYear(),
        },
        this.get(APP_KEY),
      );
      this.set(APP_KEY, this._app);
    }
    return this._app;
  }

  setLayout(name: string, value: any): boolean {
    if (typeof this.layout[name] !== 'undefined') {
      this.layout[name] = value;
      this.set(LAYOUT_KEY, this._layout);
      return true;
    }
    return false;
  }

  setApp(val: App) {
    this._app = val;
    this.set(APP_KEY, val);
  }

}
