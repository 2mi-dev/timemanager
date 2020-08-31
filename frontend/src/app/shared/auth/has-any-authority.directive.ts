import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../../core/auth/auth.service';

@Directive({
  selector: '[appHasAnyAuthority]'
})
export class HasAnyAuthorityDirective {
  private authorities: string[];

  constructor(private authService: AuthService, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {
  }


  @Input()
  set appHasAnyAuthority(value: string | string[]) {
    this.authorities = typeof value === 'string' ? [<string>value] : <string[]>value;
    this.updateView();
    // Get notified each time authentication state changes.
    this.authService.getAuthenticationState().subscribe((identity) => this.updateView());
  }

  private updateView(): void {
    this.authService.hasAnyAuthority(this.authorities).subscribe((result) => {
      this.viewContainerRef.clear();
      if (result) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      }
    });
  }
}
