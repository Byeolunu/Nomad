import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth';

type ConversationRole = 'freelancer' | 'recruiter' | 'pm';

interface ChatMessage {
  id: number;
  from: 'me' | 'them';
  text: string;
  time: string;
}

interface ConversationThread {
  id: number;
  participant: string;
  role: ConversationRole;
  mission: string;
  unread: boolean;
  messages: ChatMessage[];
}

interface Application {
  id: number;
  coverLetter: string;
  proposedBudget: number;
  estimatedDays: number;
  additionalNotes: string;
  status: string;
  appliedDate: string;
  freelancer: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    title: string;
    hourlyRate: number;
  };
}

interface Mission {
  id: number;
  title: string;
  description: string;
  budget: number;
  status: string;
  createdAt: string;
  applicationCount: number;
  applications?: Application[];
}

@Component({
  selector: 'app-recruiter-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './recruiter-dashboard.html',
  styleUrl: './recruiter-dashboard.css'
})
export class RecruiterDashboardComponent implements OnInit {
  missions: Mission[] = [];
  isLoading = true;
  error: string | null = null;
  selectedMission: Mission | null = null;
  processingApplicationId: number | null = null;
  userEmail: string = '';
  activeTab: 'overview' | 'missions' | 'proposals' | 'messages' | 'company' = 'overview';

  messageThreads: ConversationThread[] = [
    {
      id: 1,
      participant: 'Amina (Freelancer)',
      role: 'freelancer',
      mission: 'UI revamp for marketplace',
      unread: true,
      messages: [
        { id: 1, from: 'them', text: 'Shared an update on the dashboard UI.', time: '1h ago' },
        { id: 2, from: 'me', text: 'Thanks, I will review tonight.', time: '45m ago' }
      ]
    },
    {
      id: 2,
      participant: 'Youssef (Freelancer)',
      role: 'freelancer',
      mission: 'Mobile onboarding flow',
      unread: false,
      messages: [
        { id: 3, from: 'them', text: 'Can we adjust the deadline?', time: '6h ago' },
        { id: 4, from: 'me', text: 'Yes, extending by 3 days works.', time: '4h ago' }
      ]
    },
    {
      id: 3,
      participant: 'Lina (Designer)',
      role: 'freelancer',
      mission: 'Brand refresh assets',
      unread: false,
      messages: [
        { id: 5, from: 'them', text: 'Uploaded the new assets.', time: '1d ago' }
      ]
    }
  ];

  selectedThreadId: number | null = null;
  draftMessage = '';

  proposalsLoading = false;

  companyProfile = {
    name: 'TechMorocco',
    website: 'https://techmorocco.com',
    size: '51-200 employees',
    location: 'Casablanca, MA',
    blurb: 'We build modern digital products and partner with top freelancers across MENA.'
  };

  recruiterReadonly = {
    name: '',
    email: ''
  };

  companyForm = {
    website: '',
    blurb: ''
  };

  profileSaved = false;

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
    this.userEmail = this.authService.getUserEmail() || 'Recruiter';
    this.recruiterReadonly = {
      name: this.getUserName(),
      email: this.userEmail
    };
    this.companyForm = {
      website: this.companyProfile.website,
      blurb: this.companyProfile.blurb
    };
    this.selectedThreadId = this.messageThreads[0]?.id ?? null;
    this.loadMissions();
  }

  getUserName(): string {
    return this.userEmail.split('@')[0] || 'Recruiter';
  }

  getInitials(): string {
    const name = this.getUserName();
    if (!name) return 'R';
    const parts = name.split(/[._-]/).filter(Boolean);
    if (parts.length >= 2) {
      return `${parts[0].charAt(0)}${parts[1].charAt(0)}`.toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
  }

  get totalMissionsCount(): number {
    return this.missions.length;
  }

  get openMissionsCount(): number {
    return this.missions.filter(m => m.status === 'OPEN').length;
  }

  get applicationsTotal(): number {
    return this.missions.reduce((sum, mission) => sum + (mission.applicationCount || 0), 0);
  }

  get pendingApplications(): number {
    if (!this.selectedMission?.applications) return 0;
    return this.selectedMission.applications.filter(app => app.status === 'PENDING').length;
  }

  get allProposals(): any[] {
    // Aggregate all applications from all missions into a flat list for the proposals tab
    const proposals: any[] = [];
    for (const mission of this.missions) {
      if (mission.applications && Array.isArray(mission.applications)) {
        for (const app of mission.applications) {
          proposals.push({
            id: app.id,
            mission: mission.title,
            missionId: mission.id,
            freelancer: `${app.freelancer.firstName} ${app.freelancer.lastName}`,
            freelancerEmail: app.freelancer.email,
            budget: app.proposedBudget ? `${app.proposedBudget} MAD` : 'Not specified',
            status: app.status,
            submitted: this.formatTimeAgo(app.appliedDate),
            appliedDate: app.appliedDate,
            coverLetter: app.coverLetter,
            estimatedDays: app.estimatedDays
          });
        }
      }
    }
    return proposals;
  }

  setTab(tab: typeof this.activeTab): void {
    this.activeTab = tab;
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  loadMissions(): void {
    this.isLoading = true;
    this.http.get<Mission[]>('http://localhost:8090/api/missions/recruiter', { headers: this.getAuthHeaders() })
      .subscribe({
        next: (missions) => {
          this.missions = missions;
          // Load applications for all missions
          this.loadAllApplications();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading missions:', err);
          this.error = 'Failed to load missions';
          this.isLoading = false;
        }
      });
  }

  loadAllApplications(): void {
    // Load applications for each mission
    for (const mission of this.missions) {
      this.http.get<Application[]>(`http://localhost:8090/api/applications/mission/${mission.id}/proposals`, { headers: this.getAuthHeaders() })
        .subscribe({
          next: (response: any) => {
            if (response.success && response.proposals) {
              mission.applications = response.proposals.map((prop: any) => ({
                id: prop.id,
                coverLetter: prop.coverLetter,
                proposedBudget: prop.proposedBudget,
                estimatedDays: prop.estimatedDays,
                additionalNotes: prop.additionalNotes,
                status: prop.status,
                appliedDate: prop.appliedDate,
                freelancer: {
                  id: prop.freelancerId,
                  firstName: prop.freelancerName?.split(' ')[0] || 'Unknown',
                  lastName: prop.freelancerName?.split(' ')[1] || '',
                  email: prop.freelancerEmail,
                  title: 'Freelancer',
                  hourlyRate: 0
                }
              }));
              console.log(`Loaded ${response.proposals.length} proposals for mission ${mission.id}`);
            }
          },
          error: (err) => {
            console.error(`Error loading applications for mission ${mission.id}:`, err);
            mission.applications = [];
          }
        });
    }
  }

  selectMission(mission: Mission): void {
    if (this.selectedMission?.id === mission.id) {
      this.selectedMission = null;
      return;
    }
    
    // Load applications for this mission
    this.http.get<Application[]>(`http://localhost:8090/api/applications/mission/${mission.id}`, { headers: this.getAuthHeaders() })
      .subscribe({
        next: (applications) => {
          mission.applications = applications;
          this.selectedMission = mission;
        },
        error: (err) => {
          console.error('Error loading applications:', err);
        }
      });
  }

  updateApplicationStatus(applicationId: number, status: string): void {
    this.processingApplicationId = applicationId;
    
    this.http.put<Application>(
      `http://localhost:8090/api/applications/${applicationId}/status`,
      JSON.stringify(status),
      { headers: this.getAuthHeaders() }
    ).subscribe({
      next: () => {
        if (this.selectedMission?.applications) {
          const index = this.selectedMission.applications.findIndex(a => a.id === applicationId);
          if (index !== -1) {
            this.selectedMission.applications[index].status = status;
          }
        }
        this.processingApplicationId = null;
      },
      error: (err) => {
        console.error('Error updating status:', err);
        this.processingApplicationId = null;
      }
    });
  }

  acceptApplication(applicationId: number): void {
    this.updateApplicationStatus(applicationId, 'ACCEPTED');
  }

  rejectApplication(applicationId: number): void {
    this.updateApplicationStatus(applicationId, 'REJECTED');
  }

  acceptProposal(proposalId: number): void {
    this.processingApplicationId = proposalId;
    console.log('Accepting proposal:', proposalId);
    
    this.http.put<any>(
      `http://localhost:8090/api/applications/${proposalId}/accept`,
      {},
      { headers: this.getAuthHeaders() }
    ).subscribe({
      next: (response) => {
        console.log('Proposal accepted successfully:', response);
        
        // Update the proposal in the allProposals list
        const proposal = this.allProposals.find(p => p.id === proposalId);
        if (proposal) {
          proposal.status = 'ACCEPTED';
          console.log('Updated proposal status to ACCEPTED');
        }
        
        // Also update in missions applications
        for (const mission of this.missions) {
          if (mission.applications) {
            const app = mission.applications.find(a => a.id === proposalId);
            if (app) {
              app.status = 'ACCEPTED';
              console.log('Updated application status in mission:', mission.title);
            }
          }
        }
        
        this.processingApplicationId = null;
      },
      error: (err) => {
        console.error('Error accepting proposal:', err);
        alert('Failed to accept proposal');
        this.processingApplicationId = null;
      }
    });
  }

  rejectProposal(proposalId: number): void {
    this.processingApplicationId = proposalId;
    console.log('Rejecting proposal:', proposalId);
    
    this.http.put<any>(
      `http://localhost:8090/api/applications/${proposalId}/reject`,
      {},
      { headers: this.getAuthHeaders() }
    ).subscribe({
      next: (response) => {
        console.log('Proposal rejected successfully:', response);
        
        // Update the proposal in the allProposals list
        const proposal = this.allProposals.find(p => p.id === proposalId);
        if (proposal) {
          proposal.status = 'REJECTED';
          console.log('Updated proposal status to REJECTED');
        }
        
        // Also update in missions applications
        for (const mission of this.missions) {
          if (mission.applications) {
            const app = mission.applications.find(a => a.id === proposalId);
            if (app) {
              app.status = 'REJECTED';
              console.log('Updated application status in mission:', mission.title);
            }
          }
        }
        
        this.processingApplicationId = null;
      },
      error: (err) => {
        console.error('Error rejecting proposal:', err);
        alert('Failed to reject proposal');
        this.processingApplicationId = null;
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACCEPTED': return 'status-accepted';
      case 'REJECTED': return 'status-rejected';
      case 'PENDING': return 'status-pending';
      default: return '';
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  }

  viewFreelancerProfile(freelancerId: number): void {
    this.router.navigate(['/freelancers', freelancerId]);
  }

  navigateToPostMission(): void {
    this.router.navigate(['/post-mission']);
  }

  get selectedThread(): ConversationThread | null {
    if (!this.selectedThreadId) return this.messageThreads[0] || null;
    return this.messageThreads.find(t => t.id === this.selectedThreadId) || this.messageThreads[0] || null;
  }

  selectThread(threadId: number): void {
    this.selectedThreadId = threadId;
    this.messageThreads = this.messageThreads.map(thread =>
      thread.id === threadId ? { ...thread, unread: false } : thread
    );
  }

  getLastMessage(thread: ConversationThread): ChatMessage | null {
    if (!thread.messages.length) return null;
    return thread.messages[thread.messages.length - 1];
  }

  private formatTimeAgo(dateString: string): string {
    if (!dateString) return 'Recently';
    
    const date = new Date(dateString);
    const now = new Date();
    const secondsAgo = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (secondsAgo < 60) return 'Just now';
    if (secondsAgo < 3600) return `${Math.floor(secondsAgo / 60)}m ago`;
    if (secondsAgo < 86400) return `${Math.floor(secondsAgo / 3600)}h ago`;
    if (secondsAgo < 604800) return `${Math.floor(secondsAgo / 86400)}d ago`;
    
    const weeks = Math.floor(secondsAgo / 604800);
    return `${weeks}w ago`;
  }

  private formatTimeLabel(date: Date): string {
    return date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
  }

  sendMessage(): void {
    const thread = this.selectedThread;
    const text = this.draftMessage.trim();
    if (!thread || !text) return;

    const outgoing: ChatMessage = {
      id: Date.now(),
      from: 'me',
      text,
      time: this.formatTimeLabel(new Date())
    };

    this.messageThreads = this.messageThreads.map(t =>
      t.id === thread.id ? { ...t, messages: [...t.messages, outgoing], unread: false } : t
    );

    this.draftMessage = '';
  }

  saveCompany(): void {
    this.companyProfile = {
      ...this.companyProfile,
      website: this.companyForm.website,
      blurb: this.companyForm.blurb
    };
    this.profileSaved = true;
    setTimeout(() => (this.profileSaved = false), 2000);
  }
}
