import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FreelancerService, FreelancerDisplay } from '../services/freelancer.service';
import { AuthService } from '../auth/auth';

@Component({
  selector: 'app-freelancer-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './freelancer-profile.html',
  styleUrl: './freelancer-profile.css'
})
export class FreelancerProfileComponent implements OnInit {
  freelancer: FreelancerDisplay | null = null;
  isLoading = true;
  error: string | null = null;
  isLoggedIn = false;
  isRecruiter = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private freelancerService: FreelancerService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedin();
    this.isRecruiter = this.authService.isRecruiter();

    this.route.params.subscribe(params => {
      const id = params['id'];
      this.loadFreelancer(id);
    });
  }

  loadFreelancer(id: string): void {
    this.isLoading = true;
    this.error = null;

    this.freelancerService.getFreelancerById(id).subscribe({
      next: (freelancer) => {
        this.freelancer = freelancer;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading freelancer:', err);
        this.error = 'Failed to load freelancer profile.';
        this.isLoading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/freelancers']);
  }

  hireFreelancer(): void {
    if (this.isLoggedIn && this.isRecruiter) {
      this.router.navigate(['/hire', this.freelancer?.id]);
    } else {
      this.router.navigate(['/signup']);
    }
  }

  getStars(count: number): number[] {
    return Array(count).fill(0);
  }
}
