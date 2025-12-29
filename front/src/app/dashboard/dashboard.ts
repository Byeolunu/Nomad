import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  userEmail: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedin() || !this.authService.isFreelancer()) {
      this.router.navigate(['/home']);
      return;
    }
    this.userEmail = this.authService.getUserEmail() || 'Freelancer';
  }

  getUserName(): string {
    return this.userEmail.split('@')[0] || 'Freelancer';
  }
}
