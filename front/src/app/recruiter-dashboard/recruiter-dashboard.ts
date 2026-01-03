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

  proposals = [
    { id: 1, mission: 'UI revamp for marketplace', freelancer: 'Sara K.', budget: '15,000 MAD', status: 'Pending', submitted: '2d ago' },
    { id: 2, mission: 'Mobile onboarding flow', freelancer: 'Adil R.', budget: '12,500 MAD', status: 'Reviewed', submitted: '4d ago' },
    { id: 3, mission: 'Data dashboard', freelancer: 'Mina L.', budget: '18,000 MAD', status: 'Shortlisted', submitted: '1w ago' }
  ];

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
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading missions:', err);
          this.error = 'Failed to load missions';
          this.isLoading = false;
        }
      });
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
