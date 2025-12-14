import { Component } from '@angular/core';
import { AuthService } from '../auth';
import { Router } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  imports: [
    FormsModule
  ]
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';
  successMessage = '';
  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    console.log('Attempting login with:', this.email);
    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        console.log('Login successful:', response);
        this.successMessage = 'Login successful!';
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMessage = err.error || 'Login failed. Check credentials.';
      }
    });
  }
}
