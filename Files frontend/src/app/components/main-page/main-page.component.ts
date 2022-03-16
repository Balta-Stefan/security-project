import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Role } from 'src/app/models/Role';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {
  role: Role;

  constructor(private appService: ApplicationService, private router: Router) { 
    this.role = appService.getRole(); 
  }

  ngOnInit(): void {
  }

  logout(): void{
    this.appService.logout();
  }
}
