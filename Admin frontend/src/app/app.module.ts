import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HttpHeaders } from '@angular/common/http';

import { MatCardModule} from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MainPageComponent } from './components/main-page/main-page.component';
import { UsersViewComponent } from './components/users-view/users-view.component';

export const baseURL:string = "/api/v1";//"http://localhost:8080/api/v1";
export const jsonHeaders: HttpHeaders = new HttpHeaders({
  'Accept': 'application/json', 
  'Content-Type': 'application/json'
});

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    UsersViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
