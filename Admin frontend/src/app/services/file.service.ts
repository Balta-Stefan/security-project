import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { baseURL, jsonHeaders } from '../app.module';
import { FileBasicDTO } from '../models/FileBasicDTO';
import { FileLogDTO } from '../models/FileLogDTO';
import { DirectoryDTO } from '../models/DirectoryDTO';

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

  getRoot(): Observable<DirectoryDTO>{
    return this.http.get<DirectoryDTO>(`${baseURL}/files/root`, 
    {
      headers: jsonHeaders
    });
  }

  deleteFile(fileID: number): Observable<any>{
    return this.http.delete<any>(`${baseURL}/file/${fileID}`);
  }

  listDir(dirID: number): Observable<DirectoryDTO>{
    return this.http.get<DirectoryDTO>(`${baseURL}/dir/${dirID}`,
    {
      headers: jsonHeaders
    });
  }

  uploadFile(parentID: number, file: File): Observable<FileBasicDTO>{
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post<any>(`${baseURL}/files/file`, formData);
  }
}
