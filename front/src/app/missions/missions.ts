import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MissionService, MissionDisplay } from '../services/mission.service';
import { categories } from '../lib/mockData';
import { CardComponent } from '../components/ui/card/card';
import { ButtonComponent } from '../components/ui/button/button';

@Component({
  selector: 'app-missions',
  standalone: true,
  imports: [CommonModule, FormsModule, CardComponent, ButtonComponent],
  templateUrl: './missions.html',
  styleUrls: ['./missions.css']
})
export class MissionsComponent implements OnInit {
  searchQuery = '';
  selectedCategory = 'all';
  selectedExperience = 'all';
  selectedLocation = 'all';

  missions: MissionDisplay[] = [];
  categories = categories;
  isLoading = true;
  error: string | null = null;

  constructor(
    private router: Router,
    private missionService: MissionService
  ) {}

  ngOnInit() {
    this.loadMissions();
  }

  loadMissions() {
    this.isLoading = true;
    this.error = null;
    
    this.missionService.getAllMissions().subscribe({
      next: (data) => {
        this.missions = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading missions:', err);
        this.error = 'Unable to load missions. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  get filteredMissions(): MissionDisplay[] {
    return this.missions.filter(mission => {
      const matchesSearch = 
        mission.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        mission.description.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        mission.skills.some(skill => skill.toLowerCase().includes(this.searchQuery.toLowerCase()));
      
      const matchesCategory = this.selectedCategory === 'all' || mission.category === this.selectedCategory;
      const matchesExperience = this.selectedExperience === 'all' || mission.experienceLevel === this.selectedExperience;
      const matchesLocation = this.selectedLocation === 'all' || mission.location === this.selectedLocation;
      
      return matchesSearch && matchesCategory && matchesExperience && matchesLocation;
    });
  }

  get locations(): string[] {
    return Array.from(new Set(this.missions.map(m => m.location)));
  }

  clearFilters() {
    this.searchQuery = '';
    this.selectedCategory = 'all';
    this.selectedExperience = 'all';
    this.selectedLocation = 'all';
  }

  navigateToMission(missionId: string) {
    this.router.navigate(['/missions', missionId]);
  }

  navigateToSignup() {
    this.router.navigate(['/signup']);
  }
}
