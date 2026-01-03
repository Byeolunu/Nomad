import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth';
import { FreelancerService, FreelancerDisplay } from '../services/freelancer.service';
import { MissionService, MissionDisplay } from '../services/mission.service';

@Component({
  selector: 'app-hire-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hire-freelancer.html',
  styleUrl: './hire-freelancer.css'
})
export class HireFreelancerComponent implements OnInit {
  freelancer: FreelancerDisplay | null = null;
  recruiterMissions: MissionDisplay[] = [];
  isLoading = true;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  contract = {
    title: '',
    description: '',
    budget: 0,
    missionId: null as number | null,
    startDate: '',
    endDate: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
    private freelancerService: FreelancerService,
    private missionService: MissionService
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedin() || !this.authService.isRecruiter()) {
      this.router.navigate(['/login']);
      return;
    }

    this.route.params.subscribe(params => {
      const freelancerId = params['id'];
      this.loadFreelancer(freelancerId);
      this.loadRecruiterMissions();
    });
  }

  loadFreelancer(freelancerId: string) {
    this.freelancerService.getFreelancerById(freelancerId).subscribe({
      next: (data) => {
        this.freelancer = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading freelancer:', err);
        this.errorMessage = 'Freelancer not found';
        this.isLoading = false;
      }
    });
  }

  loadRecruiterMissions() {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    this.http.get<any[]>('http://localhost:8090/api/missions/recruiter', { headers })
      .subscribe({
        next: (missions) => {
          this.recruiterMissions = missions.map(m => ({
            id: m.id,
            title: m.title,
            description: m.description,
            budget: `${m.budget} MAD`,
            deadline: m.deadline,
            postedDate: m.createdAt,
            location: 'Remote',
            type: m.type || 'FIXED',
            skills: m.requiredSkills?.map((s: any) => s.name) || [],
            company: m.recruiter?.companyName || 'Your Company',
            applicants: 0,
            companyLogo: m.recruiter?.logo || '',
            category: m.category || '',
            experienceLevel: m.experienceLevel || ''
          }));
        },
        error: (err) => {
          console.error('Error loading missions:', err);
        }
      });
  }

  onSubmit() {
    if (!this.contract.title || !this.contract.description || !this.contract.budget) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const contractData: any = {
      title: this.contract.title,
      description: this.contract.description,
      budget: this.contract.budget
    };

    if (this.contract.missionId) {
      contractData.missionId = this.contract.missionId;
    }
    if (this.contract.startDate) {
      contractData.startDate = `${this.contract.startDate}T00:00:00`;
    }
    if (this.contract.endDate) {
      contractData.endDate = `${this.contract.endDate}T23:59:59`;
    }

    this.http.post(`http://localhost:8090/api/contracts/hire/${this.freelancer?.id}`, contractData, { headers })
      .subscribe({
        next: (response: any) => {
          this.successMessage = 'Hiring request sent successfully!';
          this.isSubmitting = false;
          setTimeout(() => {
            this.router.navigate(['/freelancers', this.freelancer?.id]);
          }, 2000);
        },
        error: (err) => {
          console.error('Error sending hiring request:', err);
          this.errorMessage = err.error?.message || 'Failed to send hiring request. Please try again.';
          this.isSubmitting = false;
        }
      });
  }

  goBack() {
    this.router.navigate(['/freelancers', this.freelancer?.id]);
  }
}
