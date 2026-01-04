import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface FooterLink {
  label: string;
  path: string;
}

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class Footer {
  readonly quickLinks: FooterLink[] = [
    { label: 'Home', path: '/home' },
    { label: 'Missions', path: '/missions' },
    { label: 'Freelancers', path: '/freelancers' },
  ];

  readonly accountLinks: FooterLink[] = [
    { label: 'Sign In', path: '/login' },
    { label: 'Create Account', path: '/signup' },
  ];

  readonly contactEmail = 'contact@nomad.com';
  readonly contactPhone = '+212612345678';
  readonly currentYear = new Date().getFullYear();
}
