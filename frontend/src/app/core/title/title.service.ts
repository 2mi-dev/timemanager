import { Inject, Injectable, Injector } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { DOCUMENT } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class TitleService {

  private _prefix = '';
  private _suffix = '';
  private _separator = ' - ';
  private _reverse = false;
  private _default = 'Not Page Name';

  constructor(private injector: Injector,
              private title: Title,
              @Inject(DOCUMENT) private doc: any) { }

  /** Set separator */
  set separator(value: string) {
    this._separator = value;
  }

  /** Set prefix */
  set prefix(value: string) {
    this._prefix = value;
  }

  /** Set suffix */
  set suffix(value: string) {
    this._suffix = value;
  }

  /** Set whether to reverse */
  set reverse(value: boolean) {
    this._reverse = value;
  }

  /** Set the default title name */
  set default(value: string) {
    this._default = value;
  }

  private getByElement(): string {
    const el =
      this.doc.querySelector('.content__title h1') ||
      this.doc.querySelector('.content__title nz-breadcrumb-item:last-child') ||
      this.doc.querySelector('app-page-header h1.title');
    if (el) {
      return el.firstChild.textContent.trim();
    }
    return '';
  }

  private getByRoute(): string {
    let next = this.injector.get(ActivatedRoute);
    while (next.firstChild) next = next.firstChild;
    const data = (next.snapshot && next.snapshot.data) || {};
    return data.title;
  }

  setTitle(title?: string | string[]) {
    if (!title) {
      title =
        this.getByRoute() ||
        this.getByElement() ||
        this._default;
    }
    if (title && !Array.isArray(title)) {
      title = [title];
    }

    let newTitles = [];
    if (this._prefix) {
      newTitles.push(this._prefix);
    }
    newTitles.push(...(title as string[]));
    if (this._suffix) {
      newTitles.push(this._suffix);
    }
    if (this._reverse) {
      newTitles = newTitles.reverse();
    }
    this.title.setTitle(newTitles.join(this._separator));
  }


}
