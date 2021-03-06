import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HttpHeaders } from '@angular/common/http'
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDialogModule } from '@angular/material/dialog';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { FilesViewComponent } from './components/files-view/files-view.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTreeModule } from '@angular/material/tree';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule} from '@angular/material/card';
import { LogsModalComponent } from './components/logs-modal/logs-modal.component';
import { MoveFileModalComponent } from './components/move-file-modal/move-file-modal.component';

export const baseURL:string = "/api/v1";//"http://localhost:8080/api/v1";
export const jsonHeaders: HttpHeaders = new HttpHeaders({
  'Accept': 'application/json', 
  'Content-Type': 'application/json'
});


@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    FilesViewComponent,
    LogsModalComponent,
    MoveFileModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule, 
    ReactiveFormsModule, 
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatExpansionModule,
    MatTreeModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
