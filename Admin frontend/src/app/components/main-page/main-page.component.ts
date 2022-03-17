import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  constructor(private http: HttpClient, private router: Router) { 
    
  }

  ngOnInit(): void {
    this.router.navigateByUrl("/users");
  }

  logout(): void{
    this.http.post<any>("/logout", null).subscribe();
  }
}
