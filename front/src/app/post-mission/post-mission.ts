import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth';

interface Skill {
  id: number;
  name: string;
  category: string;
}

@Component({
  selector: 'app-post-mission',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post-mission.html',
  styleUrl: './post-mission.css'
})
export class PostMissionComponent implements OnInit {
  mission = {
    title: '',
    description: '',
    budget: 0,
    deadline: '',
    type: 'FIXED' as 'FIXED' | 'HOURLY',
    requiredSkills: [] as Skill[]
  };

  availableSkills: Skill[] = [];
  selectedSkillIds: number[] = [];
  missionTypes = ['FIXED', 'HOURLY'];

  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedin() || !this.authService.isRecruiter()) {
      this.router.navigate(['/home']);
      return;
    }
    this.loadSkills();
  }

  loadSkills() {
    this.http.get<Skill[]>('http://localhost:8090/api/skills')
      .subscribe({
        next: (skills) => {
          this.availableSkills = skills;
        },
        error: (err) => {
          console.error('Error loading skills:', err);
        }
      });
  }

  toggleSkill(skill: Skill) {
    const index = this.selectedSkillIds.indexOf(skill.id);
    if (index === -1) {
      this.selectedSkillIds.push(skill.id);
    } else {
      this.selectedSkillIds.splice(index, 1);
    }
  }

  isSkillSelected(skillId: number): boolean {
    return this.selectedSkillIds.includes(skillId);
  }

  onSubmit() {
    if (!this.mission.title || !this.mission.description || !this.mission.budget) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const selectedSkills = this.availableSkills.filter(s => this.selectedSkillIds.includes(s.id));
    
    const missionData = {
      title: this.mission.title,
      description: this.mission.description,
      budget: this.mission.budget,
      type: this.mission.type,
      deadline: this.mission.deadline ? `${this.mission.deadline}T23:59:59` : null,
      requiredSkills: selectedSkills.map(s => ({ id: s.id }))
    };

    this.http.post('http://localhost:8090/api/missions', missionData, { headers })
      .subscribe({
        next: (response) => {
          this.successMessage = 'Mission posted successfully!';
          this.isSubmitting = false;
          setTimeout(() => {
            this.router.navigate(['/missions']);
          }, 2000);
        },
        error: (err) => {
          console.error('Error posting mission:', err);
          this.errorMessage = 'Failed to post mission. Please try again.';
          this.isSubmitting = false;
        }
      });
  }
}
