import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, AuthRequest } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  email = '';
  password = '';
  loading = false;
  error: string | null = null;

  constructor(private auth: AuthService) {}

  submit() {
    this.error = null;
    this.loading = true;
    const payload: AuthRequest = { email: this.email, password: this.password };
    this.auth.login(payload).subscribe({
      next: res => {
        this.loading = false;
        // redirect basic: go to missions list
        window.location.href = '/missions';
      },
      error: err => {
        this.loading = false;
        this.error = 'Invalid email or password';
      }
    });
  }
}
