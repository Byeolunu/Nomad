import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FreelancerService, FreelancerDisplay } from '../services/freelancer.service';
import { CardComponent } from '../components/ui/card/card';
import { ButtonComponent } from '../components/ui/button/button';

@Component({
  selector: 'app-freelancers',
  standalone: true,
  imports: [CommonModule, FormsModule, CardComponent, ButtonComponent],
  templateUrl: './freelancers.html',
  styleUrl: './freelancers.css'
})
export class FreelancersComponent implements OnInit {
  freelancers: FreelancerDisplay[] = [];
  filteredFreelancers: FreelancerDisplay[] = [];
  
  searchQuery = '';
  selectedLocation = 'all';
  minRating = 'all';
  
  locations: string[] = [];
  isLoading = true;
  error: string | null = null;

  constructor(
    private router: Router,
    private freelancerService: FreelancerService
  ) {}

  ngOnInit(): void {
    this.loadFreelancers();
  }

  loadFreelancers(): void {
    this.isLoading = true;
    this.error = null;
    
    this.freelancerService.getAllFreelancers().subscribe({
      next: (freelancers) => {
        this.freelancers = freelancers;
        this.filteredFreelancers = freelancers;
        this.locations = Array.from(new Set(freelancers.map(f => f.location)));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading freelancers:', err);
        this.error = 'Failed to load freelancers. Please try again.';
        this.isLoading = false;
      }
    });
  }

  filterFreelancers(): void {
    this.filteredFreelancers = this.freelancers.filter(freelancer => {
      const matchesSearch = 
        freelancer.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        freelancer.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        freelancer.skills.some(skill => skill.toLowerCase().includes(this.searchQuery.toLowerCase()));
      
      const matchesLocation = this.selectedLocation === 'all' || freelancer.location === this.selectedLocation;
      const matchesRating = this.minRating === 'all' || freelancer.rating >= parseFloat(this.minRating);
      
      return matchesSearch && matchesLocation && matchesRating;
    });
  }

  onSearchChange(): void {
    this.filterFreelancers();
  }

  onLocationChange(): void {
    this.filterFreelancers();
  }

  onRatingChange(): void {
    this.filterFreelancers();
  }

  clearFilters(): void {
    this.searchQuery = '';
    this.selectedLocation = 'all';
    this.minRating = 'all';
    this.filteredFreelancers = this.freelancers;
  }

  navigateToProfile(id: string): void {
    this.router.navigate(['/freelancers', id]);
  }

  getStars(rating: number): number[] {
    return Array(Math.floor(rating)).fill(0);
  }
}
