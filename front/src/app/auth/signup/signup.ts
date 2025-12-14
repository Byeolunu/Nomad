import { Component } from '@angular/core';
import { AuthService } from '../auth';
import { Router } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.html',
  imports: [
    FormsModule
  ]
})
export class SignupComponent {
  email = '';
  password = '';
  role = 'ROLE_USER';
  message = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSignup() {
    this.authService.signup(this.email, this.password, this.role).subscribe({
      next: () => {
        this.message = 'Signup successful! You can login now.';
        this.router.navigate(['/login']);
      },
      error: () => this.message = 'Signup failed.'
    });
  }
}
