import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8090/api/auth'; // your backend URL

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post<{ token: string, role: string }>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap(res => {
          localStorage.setItem('jwtToken', res.token);
          localStorage.setItem('role', res.role);
        })
      );
  }

  signup(email: string, password: string, role: string) {
    return this.http.post(`${this.apiUrl}/register`, { email, password, role });
  }

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('role');
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }
}
