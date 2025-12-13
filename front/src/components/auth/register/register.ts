import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, RegisterFreelancerRequest, RegisterRecruiterRequest } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  role: 'freelancer' | 'recruiter' = 'freelancer';
  firstName = '';
  lastName = '';
  email = '';
  username = '';
  password = '';
  phoneNumber = '';
  // freelancer extras
  title = '';
  summary = '';
  hourlyRate?: number;
  // recruiter extras
  companyName = '';
  companyWebsite = '';

  loading = false;
  success: string | null = null;
  error: string | null = null;

  constructor(private auth: AuthService) {}

  submit() {
    this.error = null; this.success = null; this.loading = true;

    if (this.role === 'freelancer') {
      const payload: RegisterFreelancerRequest = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        username: this.username,
        password: this.password,
        phoneNumber: this.phoneNumber,
        title: this.title,
        summary: this.summary,
        hourlyRate: this.hourlyRate
      };
      this.auth.registerFreelancer(payload).subscribe({
        next: _ => { this.loading = false; this.success = 'Freelancer registered. You can login now.'; },
        error: err => { 
          this.loading = false; 
          console.error('Registration error:', err);
          this.error = err.error?.message || err.message || 'Registration failed. Check console for details.'; 
        }
      });
    } else {
      const payload: RegisterRecruiterRequest = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        username: this.username,
        password: this.password,
        phoneNumber: this.phoneNumber,
        companyName: this.companyName,
        companyWebsite: this.companyWebsite
      };
      this.auth.registerRecruiter(payload).subscribe({
        next: _ => { this.loading = false; this.success = 'Recruiter registered. You can login now.'; },
        error: err => { 
          this.loading = false; 
          console.error('Registration error:', err);
          this.error = err.error?.message || err.message || 'Registration failed. Check console for details.'; 
        }
      });
    }
  }
}
