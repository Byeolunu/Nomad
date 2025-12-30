import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ZellijPatternComponent } from '../components/zellij-pattern/zellij-pattern';
import { categories, testimonials } from '../lib/mockData';
import { AuthService } from '../auth/auth';
import { Subscription } from 'rxjs';
import { CardComponent } from '../components/ui/card/card';
import { ButtonComponent } from '../components/ui/button/button';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ZellijPatternComponent,
    CardComponent,
    ButtonComponent
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  searchQuery = '';
  categories = categories;
  testimonials = testimonials;
  
  isLoggedIn = false;
  userRole: string | null = null;
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private authService: AuthService
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

  handleSearch(event: Event) {
    event.preventDefault();
    this.router.navigate(['/missions']);
  }

  navigateToSignup() {
    this.router.navigate(['/signup']);
  }

  navigateToMissions() {
    this.router.navigate(['/missions']);
  }

  navigateToPostMission() {
    this.router.navigate(['/post-mission']);
  }

  navigateToDashboard() {
    if (this.userRole === 'RECRUITER') {
      this.router.navigate(['/recruiter-dashboard']);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }

  navigateToFreelancers() {
    this.router.navigate(['/freelancers']);
  }

  getIcon(iconName: string): string {
    const iconMap: Record<string, string> = {
      'Code2': '⌨',
      'Palette': '◆',
      'PenTool': '✎',
      'Megaphone': '◈',
      'Video': '▶',
      'Languages': '◎',
      'Database': '◉'
    };
    return iconMap[iconName] || '○';
  }
}

