import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { SignupComponent } from './auth/signup/signup';
import { HomeComponent } from './home/home';
import { MissionsComponent } from './missions/missions';
import { MissionDetailComponent } from './mission-detail/mission-detail';
import { FreelancersComponent } from './freelancers/freelancers';
import { FreelancerProfileComponent } from './freelancer-profile/freelancer-profile';
import { DashboardComponent } from './dashboard/dashboard';
import { PostMissionComponent } from './post-mission/post-mission';
import { ApplyMissionComponent } from './apply-mission/apply-mission';
import { HireFreelancerComponent } from './hire-freelancer/hire-freelancer';

export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'missions', component: MissionsComponent },
  { path: 'missions/:id', component: MissionDetailComponent },
  { path: 'freelancers', component: FreelancersComponent },
  { path: 'freelancers/:id', component: FreelancerProfileComponent },
  { path: 'hire/:id', component: HireFreelancerComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'post-mission', component: PostMissionComponent },
  { path: 'apply/:id', component: ApplyMissionComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];
