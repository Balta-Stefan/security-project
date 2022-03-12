import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { baseURL, jsonHeaders } from '../app.module';
import { FileBasicDTO } from '../models/FileBasicDTO';
import { FileLogDTO } from '../models/FileLogDTO';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) { }

  getFileLogs(fileID: number): Observable<FileLogDTO[]>{
    return this.http.get<FileLogDTO[]>(`${baseURL}/files/${fileID}/logs`, 
    {
      headers: jsonHeaders
    });
  }

  getRoot(): Observable<FileBasicDTO[]>{
    return this.http.get<FileBasicDTO[]>(`${baseURL}/files/root`, 
    {
      headers: jsonHeaders
    });
  }
}
