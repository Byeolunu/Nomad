import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreateReviewRequest {
  freelancerId: number;
  rating: number;
  comment: string;
}

export interface ReviewResponse {
  id: number;
  rating: number;
  comment: string;
  authorName: string;
  createdAt: string;
  freelancerId: number;
}

export interface CanReviewResponse {
  canReview: boolean;
  hasReviewed: boolean;
  hasWorkedTogether: boolean;
  existingReviewId: number | null;
}

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8090/api/reviews';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createReview(freelancerId: number, rating: number, comment: string): Observable<{ id: number; message: string }> {
    const body: CreateReviewRequest = { freelancerId, rating, comment };
    return this.http.post<{ id: number; message: string }>(this.apiUrl, body, { headers: this.getAuthHeaders() });
  }

  updateReview(reviewId: number, rating: number, comment: string): Observable<{ id: number; message: string }> {
    const body = { rating, comment };
    return this.http.put<{ id: number; message: string }>(`${this.apiUrl}/${reviewId}`, body, { headers: this.getAuthHeaders() });
  }

  getFreelancerReviews(freelancerId: number): Observable<ReviewResponse[]> {
    return this.http.get<ReviewResponse[]>(`${this.apiUrl}/freelancer/${freelancerId}`);
  }

  canReviewFreelancer(freelancerId: number): Observable<CanReviewResponse> {
    return this.http.get<CanReviewResponse>(`${this.apiUrl}/can-review/${freelancerId}`, { headers: this.getAuthHeaders() });
  }

  deleteReview(reviewId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/${reviewId}`, { headers: this.getAuthHeaders() });
  }
}
