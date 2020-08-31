import { Inject, Injectable, InjectionToken } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { WINDOW } from '../core.module';


@Injectable({
  providedIn: 'root'
})
export class ScrollService {

  constructor(
    @Inject(WINDOW) private win: any,
    @Inject(DOCUMENT) private doc: any,
  ) {}

  /**
   * Set the scroll bar to the specified element
   * @param element specifies the element, default `document.body`
   * @param topOffset offset value, default `0`
   */
  scrollToElement(element?: Element, topOffset = 0) {
    if (!element) element = this.doc.body;

    element.scrollIntoView();

    const w = this.win;
    if (w && w.scrollBy) {
      w.scrollBy(0, element.getBoundingClientRect().top - topOffset);

      if (w.pageYOffset < 20) {
        w.scrollBy(0, -w.pageYOffset);
      }
    }
  }

  /**
   * Scroll to the top
   * @param topOffset offset value, default `0`
   */
  scrollToTop(topOffset = 0) {
    this.scrollToElement(this.doc.body, topOffset);
  }
}
