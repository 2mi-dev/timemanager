import { Component, OnInit } from '@angular/core';



const data = [
  { year: '1951', sales: 38 },
  { year: '1952', sales: 52 },
  { year: '1956', sales: 61 },
  { year: '1957', sales: 145 },
  { year: '1958', sales: 48 },
  { year: '1959', sales: 38 },
  { year: '1960', sales: 38 },
  { year: '1962', sales: 38 },
];

const scale = [{
  dataKey: 'sales',
  tickInterval: 20,
}];

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.less']
})
export class DashboardComponent implements OnInit {

  forceFit: boolean = true;
  height: number = 300;
  data = data;
  scale = scale;

  constructor() { }

  ngOnInit() {
  }

}
