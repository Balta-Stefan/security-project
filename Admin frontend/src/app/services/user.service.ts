import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseURL, jsonHeaders } from '../app.module';
import { Role } from '../models/Role';
import { UserInfoDTO } from '../models/UserInfoDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  filterUsers(username?: string, role?: Role): Observable<UserInfoDTO[]>{
    let params: HttpParams = new HttpParams();
    if(username){
      params = params.append("username", username);
    }
    if(role){
      params = params.append("role", role);
    }
    
    return this.http.get<UserInfoDTO[]>(`${baseURL}/user`, {
      headers: jsonHeaders,
      params: params
    })
  }

  getUser(id: number): Observable<UserInfoDTO>{
    return this.http.get<UserInfoDTO>(`${baseURL}/user/${id}`, {
      headers: jsonHeaders
    });
  }

  changeUser(userInfo: UserInfoDTO): Observable<UserInfoDTO>{
    return this.http.put<UserInfoDTO>(`${baseURL}/user/${userInfo.id}`, userInfo, {
      headers: jsonHeaders
    });
  }
}
