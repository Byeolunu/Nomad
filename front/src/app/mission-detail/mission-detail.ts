import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MissionService, MissionDisplay } from '../services/mission.service';
import { AuthService } from '../auth/auth';

@Component({
  selector: 'app-mission-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mission-detail.html',
  styleUrls: ['./mission-detail.css']
})
export class MissionDetailComponent implements OnInit {
  mission: MissionDisplay | undefined;
  similarMissions: MissionDisplay[] = [];
  isLoading = true;
  error: string | null = null;
  
  isLoggedIn = false;
  isFreelancer = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private missionService: MissionService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.isLoggedIn = this.authService.isLoggedin();
    this.isFreelancer = this.authService.isFreelancer();
    
    this.route.params.subscribe(params => {
      const missionId = params['id'];
      this.loadMission(missionId);
    });
  }

  loadMission(missionId: string) {
    this.isLoading = true;
    this.error = null;

    this.missionService.getMissionById(missionId).subscribe({
      next: (data) => {
        this.mission = data;
        this.loadSimilarMissions();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading mission:', err);
        this.error = 'Mission not found';
        this.isLoading = false;
      }
    });
  }

  loadSimilarMissions() {
    if (!this.mission) return;

    this.missionService.getAllMissions().subscribe({
      next: (missions) => {
        this.similarMissions = missions
          .filter(m => m.id !== this.mission!.id && m.category === this.mission!.category)
          .slice(0, 3);
      },
      error: (err) => {
        console.error('Error loading similar missions:', err);
      }
    });
  }

  goBack() {
    this.router.navigate(['/missions']);
  }

  navigateToMission(missionId: string) {
    this.router.navigate(['/missions', missionId]);
  }

  applyForMission() {
    if (this.isLoggedIn && this.isFreelancer) {
      this.router.navigate(['/apply', this.mission?.id]);
    } else if (this.isLoggedIn) {
      alert('Only freelancers can apply for missions');
    } else {
      this.router.navigate(['/signup']);
    }
  }

  navigateToCompanyProfile() {
    if (this.mission?.recruiter?.id) {
      this.router.navigate(['/recruiter-profile', this.mission.recruiter.id]);
    } else {
      this.router.navigate(['/recruiter-profile', '1']);
    }
  }
}
