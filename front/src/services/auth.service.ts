import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  role: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
}

export interface RegisterFreelancerRequest {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
  phoneNumber?: string;
  title?: string;
  summary?: string;
  hourlyRate?: number;
}

export interface RegisterRecruiterRequest {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
  phoneNumber?: string;
  companyName?: string;
  companyWebsite?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8090/api/auth';
  private tokenKey = 'nomad_token';

  constructor(private http: HttpClient) {}

  login(payload: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, payload).pipe(
      tap(res => this.setToken(res.token))
    );
  }

  registerFreelancer(payload: RegisterFreelancerRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/register/freelancer`, payload);
  }

  registerRecruiter(payload: RegisterRecruiterRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/register/recruiter`, payload);
  }

  validate(): Observable<any> {
    return this.http.get(`${this.baseUrl}/validate`);
  }

  setToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  clearToken() {
    localStorage.removeItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
