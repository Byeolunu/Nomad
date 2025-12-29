import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8090/api/auth';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  private userRolee = new BehaviorSubject<string | null>(this.getRole());

  isLoggedIn = this.loggedIn.asObservable();
  userRole = this.userRolee.asObservable();

  constructor(private http: HttpClient) {}

  private hasToken(): boolean {
    return !!localStorage.getItem('jwtToken');
  }

  login(email: string, password: string) {
    return this.http.post<{ token: string, role: string, userId: number, email: string }>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap(res => {
          localStorage.setItem('jwtToken', res.token);
          localStorage.setItem('role', res.role);
          localStorage.setItem('userId', res.userId?.toString() || '');
          localStorage.setItem('userEmail', res.email || email);
          this.loggedIn.next(true);
          this.userRolee.next(res.role);
        })
      );
  }

  signup(email: string, password: string, role: string) {
    const endpoint = role === 'RECRUITER' ? 'register/recruiter' : 'register/freelancer';
    return this.http.post(`${this.apiUrl}/${endpoint}`, { email, password, role });
  }

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('userEmail');
    this.loggedIn.next(false);
    this.userRolee.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getUserId(): string | null {
    return localStorage.getItem('userId');
  }

  getUserEmail(): string | null {
    return localStorage.getItem('userEmail');
  }

  isLoggedin(): boolean {
    return this.hasToken();
  }

  isFreelancer(): boolean {
    return this.getRole() === 'FREELANCER';
  }

  isRecruiter(): boolean {
    return this.getRole() === 'RECRUITER';
  }
}
