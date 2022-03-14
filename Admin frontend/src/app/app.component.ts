import { Component } from '@angular/core';
import { ApplicationService } from './services/application.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'admin-frontend';

  constructor(private appService: ApplicationService){
    const cookies: string[] = document.cookie.split('; ');
    for(let i = 0; i < cookies.length; i++){
      if(cookies[i].startsWith('role')){
        appService.setRole(cookies[i].substring(5));
        break;
      }
    }
  }
}
