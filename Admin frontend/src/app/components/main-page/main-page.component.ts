import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  constructor(private http: HttpClient) { 
    
  }

  ngOnInit(): void {
  }

  logout(): void{
    this.http.post<any>("/logout", null).subscribe();
  }
}
