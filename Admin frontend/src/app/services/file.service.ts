import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { baseURL, jsonHeaders } from '../app.module';
import { FileBasicDTO } from '../models/FileBasicDTO';
import { FileLogDTO } from '../models/FileLogDTO';
import { DirectoryDTO } from '../models/DirectoryDTO';
import { DirectoryBasicDTO } from '../models/DirectoryBasicDTO';

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
    return this.http.delete<any>(`${baseURL}/files/file/${fileID}`);
  }

  listDir(dirID: number): Observable<DirectoryDTO>{
    return this.http.get<DirectoryDTO>(`${baseURL}/files/dir/${dirID}`,
    {
      headers: jsonHeaders
    });
  }

  uploadFile(parentID: number, file: File): Observable<FileBasicDTO>{
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post<any>(`${baseURL}/files/dir/${parentID}/children`, formData);
  }

  createDirectory(parentID: number, name: string):  Observable<FileBasicDTO>{
    let params: HttpParams = new HttpParams();
    params = params.append('name', name);

    return this.http.post<any>(`${baseURL}/files/dir/${parentID}/mkdir`, null,
    {
      params: params
    });
  }

  moveFile(fileID: number, destinationID: number): Observable<any>{
    return this.http.patch<any>(`${baseURL}/files/file/${fileID}/parent/${destinationID}`, null);
  }
}
