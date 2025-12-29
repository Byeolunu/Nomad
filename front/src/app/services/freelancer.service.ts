import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, catchError, of } from 'rxjs';

export interface BackendFreelancer {
  id: number;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  phoneNumber: string;
  createdAt: string;
  isActive: boolean;
  role: string;
  title: string;
  summary: string;
  hourlyRate: number;
  profile?: {
    id: number;
    bio: string;
    skills: { id: number; name: string; category: string }[];
    experienceLevel: string;
    location: string;
    profilePicture: string;
    workExperiences: any[];
    educations: any[];
  };
  skills: { id: number; name: string; category: string }[];
  applications: any[];
  portfolios?: BackendPortfolio[];
  reviews?: BackendReview[];
}

export interface BackendPortfolio {
  id: number;
  title: string;
  description: string;
  imageUrl: string;
  projectUrl: string;
}

export interface BackendReview {
  id: number;
  rating: number;
  comment: string;
  authorName: string;
  createdAt: string;
  recruiter?: {
    id: number;
    firstName: string;
    lastName: string;
    companyName: string;
  };
  mission?: {
    id: number;
    title: string;
  };
}
export interface FreelancerDisplay {
  id: string;
  name: string;
  title: string;
  email: string;   
  avatar: string;
  bio: string;
  location: string;
  rating: number;
  reviewCount: number;
  hourlyRate: number;
  completedProjects: number;
  skills: string[];
  portfolio: PortfolioProject[];
  reviews: Review[];
}

export interface PortfolioProject {
  id: string;
  title: string;
  description: string;
  image: string;
  projectUrl: string;
}

export interface Review {
  id: string;
  author: string;
  date: string;
  rating: number;
  comment: string;
}

@Injectable({
  providedIn: 'root'
})
export class FreelancerService {
  private apiUrl = 'http://localhost:8090/api/users';

  constructor(private http: HttpClient) {}

  getAllFreelancers(): Observable<FreelancerDisplay[]> {
    return this.http.get<BackendFreelancer[]>(`${this.apiUrl}/freelancers`).pipe(
      map(freelancers => freelancers.map(f => this.transformFreelancer(f))),
      catchError(error => {
        console.error('Error fetching freelancers:', error);
        throw error;
      })
    );
  }

  getFreelancerById(id: string): Observable<FreelancerDisplay | null> {
    return this.http.get<BackendFreelancer>(`${this.apiUrl}/${id}`).pipe(
      map(freelancer => this.transformFreelancer(freelancer)),
      catchError(error => {
        console.error('Error fetching freelancer:', error);
        return of(null);
      })
    );
  }

  private transformFreelancer(backend: BackendFreelancer): FreelancerDisplay {
    const name = `${backend.firstName} ${backend.lastName}`;
    const initials = `${backend.firstName?.charAt(0) || ''}${backend.lastName?.charAt(0) || ''}`.toUpperCase();
    const skills = backend.skills?.map(s => s.name) || 
                   backend.profile?.skills?.map(s => s.name) || [];
    const location = backend.profile?.location || 'Morocco';
    const bio = backend.profile?.bio || backend.summary || 'Experienced professional ready to work on your projects.';
    const completedProjects = backend.applications?.filter(
      (app: any) => app.status === 'ACCEPTED' || app.status === 'COMPLETED'
    ).length || 0;

    return {
      id: backend.id.toString(),
      name,
      title: backend.title || 'Freelancer',
      avatar: initials || 'FL',
      bio,
      location,
      rating: this.calculateRating(backend),
      reviewCount: this.getReviewCount(backend),
      hourlyRate: backend.hourlyRate || 200,
      completedProjects,
      skills,
      portfolio: this.generatePortfolio(backend),
      reviews: this.generateReviews(backend),
      email: backend.email
    };
  }

  private calculateRating(freelancer: BackendFreelancer): number {
    if (freelancer.reviews && freelancer.reviews.length > 0) {
      const totalRating = freelancer.reviews.reduce((sum, r) => sum + r.rating, 0);
      return Math.round((totalRating / freelancer.reviews.length) * 10) / 10;
    }
    return 4.5;
  }

  private getReviewCount(freelancer: BackendFreelancer): number {
    return freelancer.reviews?.length || 0;
  }

  private generatePortfolio(freelancer: BackendFreelancer): PortfolioProject[] {
    if (freelancer.portfolios && freelancer.portfolios.length > 0) {
      return freelancer.portfolios.map((p) => ({
        id: p.id.toString(),
        title: p.title,
        description: p.description || '',
        image: p.imageUrl,
        projectUrl: p.projectUrl || '#'
      }));
    }
    return [];
  }

  private generateReviews(freelancer: BackendFreelancer): Review[] {
    if (freelancer.reviews && freelancer.reviews.length > 0) {
      return freelancer.reviews.map(r => ({
        id: r.id.toString(),
        author: r.authorName || (r.recruiter ? `${r.recruiter.firstName} ${r.recruiter.lastName}` : 'Anonymous'),
        date: this.formatReviewDate(r.createdAt),
        rating: r.rating,
        comment: r.comment
      }));
    }
    return [];
  }

  private formatReviewDate(dateString: string): string {
    if (!dateString) return 'Recently';
    const date = new Date(dateString);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${months[date.getMonth()]} ${date.getFullYear()}`;
  }
}
