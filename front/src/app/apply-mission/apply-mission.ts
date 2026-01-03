import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth';
import { MissionService, MissionDisplay } from '../services/mission.service';

@Component({
  selector: 'app-apply-mission',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './apply-mission.html',
  styleUrl: './apply-mission.css'
})
export class ApplyMissionComponent implements OnInit {
  mission: MissionDisplay | undefined;
  isLoading = true;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  application = {
    coverLetter: '',
    proposedBudget: 0,
    estimatedDays: 0,
    additionalNotes: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
    private missionService: MissionService
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedin() || !this.authService.isFreelancer()) {
      this.router.navigate(['/login']);
      return;
    }

    this.route.params.subscribe(params => {
      const missionId = params['id'];
      this.loadMission(missionId);
    });
  }

  loadMission(missionId: string) {
    this.isLoading = true;
    this.missionService.getMissionById(missionId).subscribe({
      next: (data) => {
        this.mission = data;
        this.application.proposedBudget = parseFloat(data.budget.replace(/[^0-9.]/g, '')) || 0;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading mission:', err);
        this.errorMessage = 'Mission not found';
        this.isLoading = false;
      }
    });
  }

  onSubmit() {
    if (!this.application.coverLetter || !this.application.proposedBudget) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const applicationData = {
      coverLetter: this.application.coverLetter,
      proposedBudget: this.application.proposedBudget,
      estimatedDays: this.application.estimatedDays || null,
      additionalNotes: this.application.additionalNotes || null
    };

    this.http.post(`http://localhost:8090/api/applications/mission/${this.mission?.id}`, applicationData, { headers })
      .subscribe({
        next: (response) => {
          this.successMessage = 'Application submitted successfully!';
          this.isSubmitting = false;
          setTimeout(() => {
            this.router.navigate(['/missions']);
          }, 2000);
        },
        error: (err) => {
          console.error('Error submitting application:', err);
          if (err.error?.message) {
            this.errorMessage = err.error.message;
          } else {
            this.errorMessage = 'Failed to submit application. Please try again.';
          }
          this.isSubmitting = false;
        }
      });
  }

  goBack() {
    this.router.navigate(['/missions', this.mission?.id]);
  }
}
