import { Component } from '@angular/core';
import { AuthService } from '../auth';
import { Router } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  imports: [
    FormsModule
  ]
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.email, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']), // or homepage
      error: () => this.errorMessage = 'Login failed. Check credentials.'
    });
  }
}
