import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Role } from '../models/Role';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  role!: Role;

  constructor(private http: HttpClient) { }

  setRole(role: string): void{
    this.role = role as Role;
  }

  getRole(): Role{
    return this.role;
  }

  logout(): void{
    this.http.post<any>("/logout", null).subscribe();
  }
}
