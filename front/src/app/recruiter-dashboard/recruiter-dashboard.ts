import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth';

interface Application {
  id: number;
  coverLetter: string;
  proposedBudget: number;
  estimatedDays: number;
  additionalNotes: string;
  status: string;
  appliedDate: string;
  freelancer: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    title: string;
    hourlyRate: number;
  };
}

interface Mission {
  id: number;
  title: string;
  description: string;
  budget: number;
  status: string;
  createdAt: string;
  applicationCount: number;
  applications?: Application[];
}

@Component({
  selector: 'app-recruiter-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './recruiter-dashboard.html',
  styleUrl: './recruiter-dashboard.css'
})
export class RecruiterDashboardComponent implements OnInit {
  missions: Mission[] = [];
  isLoading = true;
  error: string | null = null;
  selectedMission: Mission | null = null;
  processingApplicationId: number | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedin() || !this.authService.isRecruiter()) {
      this.router.navigate(['/home']);
      return;
    }
    this.loadMissions();
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  loadMissions(): void {
    this.isLoading = true;
    this.http.get<Mission[]>('http://localhost:8090/api/missions/recruiter', { headers: this.getAuthHeaders() })
      .subscribe({
        next: (missions) => {
          this.missions = missions;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading missions:', err);
          this.error = 'Failed to load missions';
          this.isLoading = false;
        }
      });
  }

  selectMission(mission: Mission): void {
    if (this.selectedMission?.id === mission.id) {
      this.selectedMission = null;
      return;
    }
    
    // Load applications for this mission
    this.http.get<Application[]>(`http://localhost:8090/api/applications/mission/${mission.id}`, { headers: this.getAuthHeaders() })
      .subscribe({
        next: (applications) => {
          mission.applications = applications;
          this.selectedMission = mission;
        },
        error: (err) => {
          console.error('Error loading applications:', err);
        }
      });
  }

  updateApplicationStatus(applicationId: number, status: string): void {
    this.processingApplicationId = applicationId;
    
    this.http.put<Application>(
      `http://localhost:8090/api/applications/${applicationId}/status`,
      JSON.stringify(status),
      { headers: this.getAuthHeaders() }
    ).subscribe({
      next: () => {
        if (this.selectedMission?.applications) {
          const index = this.selectedMission.applications.findIndex(a => a.id === applicationId);
          if (index !== -1) {
            this.selectedMission.applications[index].status = status;
          }
        }
        this.processingApplicationId = null;
      },
      error: (err) => {
        console.error('Error updating status:', err);
        this.processingApplicationId = null;
      }
    });
  }

  acceptApplication(applicationId: number): void {
    this.updateApplicationStatus(applicationId, 'ACCEPTED');
  }

  rejectApplication(applicationId: number): void {
    this.updateApplicationStatus(applicationId, 'REJECTED');
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACCEPTED': return 'status-accepted';
      case 'REJECTED': return 'status-rejected';
      case 'PENDING': return 'status-pending';
      default: return '';
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  }

  viewFreelancerProfile(freelancerId: number): void {
    this.router.navigate(['/freelancers', freelancerId]);
  }

  navigateToPostMission(): void {
    this.router.navigate(['/post-mission']);
  }
}
