import { Component, OnInit } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd';

@Component({
  selector: 'app-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrls: ['./forbidden.component.less']
})
export class ForbiddenComponent implements OnInit {

  constructor(modalService: NzModalService) {
    modalService.closeAll();
  }

  ngOnInit() {
  }

}
