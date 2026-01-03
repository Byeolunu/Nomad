import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header implements OnInit, OnDestroy {
  isLoggedIn = false;
  userRole: string | null = null;
  private subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.subscriptions.push(
      this.authService.isLoggedIn.subscribe(status => {
        this.isLoggedIn = status;
      }),
      this.authService.userRole.subscribe(role => {
        this.userRole = role;
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/home']);
  }

  getUserInitial(): string {
    const email = this.authService.getUserEmail();
    return email ? email.charAt(0).toUpperCase() : 'U';
  }
}

