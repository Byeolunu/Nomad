import { Component } from '@angular/core';
import { AuthService } from '../auth';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.html',
  styleUrl: './signup.css',
  imports: [
    FormsModule,
    RouterLink
  ]
})
export class SignupComponent {
  email = '';
  password = '';
  role = 'FREELANCER';
  message = '';
  successMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSignup() {
    console.log('Attempting signup:', this.email, this.role);
    this.authService.signup(this.email, this.password, this.role).subscribe({
      next: (response) => {
        this.successMessage='Signup successful! You can login now.';
        console.log('Signup successful:', response);
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        console.error('Signup error:', err);
        this.message = 'Signup failed: ' + (err.error || err.message);
      }
    });
  }
}
