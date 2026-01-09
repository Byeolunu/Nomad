import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth';
import { FreelancerService, BackendFreelancer, BackendPortfolio } from '../services/freelancer.service';

type ConversationRole = 'recruiter' | 'pm' | 'freelancer';

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

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  userEmail: string = '';
  activeTab: 'overview' | 'missions' | 'proposals' | 'messages' | 'profile' | 'offers' = 'overview';
  userName: string = '';
  readonlyName: string = '';
  readonlyEmail: string = '';
  profileLoading = false;
  profileError: string | null = null;
  profileSaved = false;
  profileForm = {
    name:'',
    phoneNumber: '',
    title: '',
    hourlyRate: 0,
    summary: '',
    bio: '',
    location: '',
    profilePicture: '',
    experienceLevel: ''
  };
  experienceLevels = ['JUNIOR', 'MID', 'SENIOR'];
  portfolios: BackendPortfolio[] = [];
  newPortfolio: Partial<BackendPortfolio> = {
    title: '',
    description: '',
    imageUrl: '',
    projectUrl: ''
  };
  portfolioLoading = false;
  portfolioError: string | null = null;
  freelancerRaw: BackendFreelancer | null = null;

  stats = [
    { label: 'Active missions', value: 2, trend: '+1 this week' },
    { label: 'Proposals sent', value: 5, trend: '2 pending' },
    { label: 'Invites', value: 1, trend: 'New today' },
    { label: 'Messages', value: 3, trend: 'Unread' }
  ];

  activeMissions: any[] = [];
  missionsLoading = false;
  missionsError: string | null = null;

  proposals: any[] = [];
  proposalsLoading = false;
  proposalsError: string | null = null;

  offers: any[] = [];
  offersLoading = false;
  offersError: string | null = null;

  messageThreads: ConversationThread[] = [
    {
      id: 1,
      participant: 'Sofia (Recruiter)',
      role: 'recruiter',
      mission: 'UI revamp for marketplace',
      unread: true,
      messages: [
        { id: 1, from: 'them', text: 'Can you share a quick estimate for the revised scope?', time: '3h ago' },
        { id: 2, from: 'me', text: 'I can ship a draft by tomorrow morning.', time: '2h ago' }
      ]
    },
    {
      id: 2,
      participant: 'Amine (PM)',
      role: 'pm',
      mission: 'Mobile onboarding flow',
      unread: false,
      messages: [
        { id: 3, from: 'them', text: 'Approved the latest designs. Let’s prep handoff.', time: '1d ago' },
        { id: 4, from: 'me', text: 'Great, I will annotate the files and send.', time: '22h ago' }
      ]
    },
    {
      id: 3,
      participant: 'Lina (Recruiter)',
      role: 'recruiter',
      mission: 'Payments integration',
      unread: false,
      messages: [
        { id: 5, from: 'them', text: 'Let’s schedule a call tomorrow.', time: '2d ago' }
      ]
    }
  ];

  selectedThreadId: number | null = null;
  draftMessage = '';

  profile = {
    title: 'Senior Full Stack Developer',
    location: 'Casablanca, MA',
    rate: '350 MAD/hr',
    availability: '20 hrs/week',
    summary: 'I build resilient frontends and scalable backends with Angular, Spring Boot, and cloud-native tooling.'
  };

  constructor(
    private authService: AuthService,
    private router: Router,
    private freelancerService: FreelancerService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    if (!this.authService.isLoggedin() || !this.authService.isFreelancer()) {
      this.router.navigate(['/home']);
      return;
    }
    this.userEmail = this.authService.getUserEmail() || 'Freelancer';
    this.userName = this.getUserName();
    this.readonlyEmail = this.userEmail;
    this.readonlyName = this.userName;
    this.selectedThreadId = this.messageThreads[0]?.id ?? null;
    this.loadProfile();
    this.loadPortfolios();
    this.loadProposals();
    this.loadActiveMissions();
    this.loadOffers();
  }

  getUserName(): string {
    return this.userEmail.split('@')[0] || 'Freelancer';
  }

  getInitials(): string {
    const name = this.readonlyName || this.getUserName();
    if (!name) return 'F';
    const parts = name.split(/\s+/).filter(Boolean);
    if (parts.length >= 2) {
      return `${parts[0].charAt(0)}${parts[parts.length - 1].charAt(0)}`.toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
  }

  goToMissions(): void {
    this.router.navigate(['/missions']);
  }

  loadProfile(): void {
    const userId = this.authService.getUserId();
    this.profileLoading = true;
    this.profileSaved = false;

    const handleFreelancer = (freelancer: BackendFreelancer | null) => {
      if (!freelancer) {
        this.profileError = 'Freelancer not found';
        this.profileLoading = false;
        return;
      }
      this.freelancerRaw = freelancer;
      this.profileForm.name= `${freelancer.firstName || ''} ${freelancer.lastName || ''}`.trim() || this.userName;
      this.profileForm.phoneNumber = freelancer.phoneNumber || '';
      this.profileForm.title = freelancer.title || '';
      this.profileForm.hourlyRate = freelancer.hourlyRate || 0;
      this.profileForm.summary = freelancer.summary || '';
      this.profileForm.bio = freelancer.profile?.bio || '';
      this.profileForm.location = freelancer.profile?.location || '';
      this.profileForm.profilePicture = freelancer.profile?.profilePicture || '';
      this.profileForm.experienceLevel = freelancer.profile?.experienceLevel || '';
      this.readonlyName = `${freelancer.firstName || ''} ${freelancer.lastName || ''}`.trim() || this.userName;
      this.readonlyEmail = freelancer.email || this.userEmail;
      // Don't overwrite portfolios here - let loadPortfolios handle it
      this.profileError = null;
      this.profileLoading = false;
    };

    if (userId) {
      this.freelancerService.getFreelancerRaw(userId).subscribe({
        next: handleFreelancer,
        error: (err) => {
          console.error('Error loading profile', err);
          this.profileError = 'Failed to load profile';
          this.profileLoading = false;
        }
      });
    } else if (this.userEmail) {
      this.freelancerService.getFreelancerByEmail(this.userEmail).subscribe({
        next: handleFreelancer,
        error: (err) => {
          console.error('Error loading profile by email', err);
          this.profileError = 'Failed to load profile';
          this.profileLoading = false;
        }
      });
    } else {
      this.profileError = 'No user id found';
      this.profileLoading = false;
    }
  }

  loadPortfolios(): void {
    const userId = this.authService.getUserId();
    if (!userId) {
      console.warn('No user ID found for loading portfolios');
      this.portfolios = [];
      return;
    }

    console.log('Loading portfolios for user:', userId);
    this.portfolioLoading = true;
    this.portfolioError = null;
    this.freelancerService.getPortfolios(userId).subscribe({
      next: (items) => {
        console.log('Portfolios loaded successfully:', items);
        this.portfolios = items || [];
        this.portfolioLoading = false;
        this.portfolioError = null;
      },
      error: (err) => {
        console.error('Error loading portfolios:', err);
        console.error('Error status:', err.status);
        console.error('Error details:', err.error);
        // Empty portfolio is valid - only show error for actual server failures
        this.portfolios = [];
        this.portfolioLoading = false;
        // Only show error if it's a server error (not 404)
        if (err.status && err.status !== 404) {
          this.portfolioError = 'Failed to load portfolio: ' + (err.error?.error || err.message);
        }
      }
    });
  }

  addPortfolio(): void {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.portfolioError = 'Please log in to add projects';
      return;
    }
    if (!this.newPortfolio.title?.trim()) {
      this.portfolioError = 'Title is required to add a project';
      setTimeout(() => this.portfolioError = null, 3000);
      return;
    }
    console.log('Adding portfolio for user:', userId, this.newPortfolio);
    this.portfolioLoading = true;
    this.portfolioError = null;
    this.freelancerService.addPortfolio(userId, this.newPortfolio).subscribe({
      next: (created) => {
        console.log('Portfolio created successfully:', created);
        this.portfolios = [created, ...this.portfolios];
        this.newPortfolio = { title: '', description: '', imageUrl: '', projectUrl: '' };
        this.portfolioLoading = false;
        this.portfolioError = null;
      },
      error: (err) => {
        console.error('Error adding portfolio:', err);
        console.error('Error status:', err.status);
        console.error('Error details:', err.error);
        this.portfolioError = 'Failed to add project: ' + (err.error?.error || err.message);
        this.portfolioLoading = false;
      }
    });
  }

  deletePortfolio(id: number | undefined): void {
    if (!id) return;
    if (!confirm('Are you sure you want to delete this project?')) return;
    this.portfolioLoading = true;
    this.portfolioError = null;
    this.freelancerService.deletePortfolio(id).subscribe({
      next: () => {
        this.portfolios = this.portfolios.filter(p => p.id !== id);
        this.portfolioLoading = false;
      },
      error: (err) => {
        console.error('Error deleting portfolio', err);
        this.portfolioError = 'Failed to delete project';
        this.portfolioLoading = false;
      }
    });
  }

  saveProfile(): void {
    if (!this.freelancerRaw) return;
    this.profileLoading = true;
    this.profileSaved = false;
    const payload = {
      name: this.profileForm.name,
      phoneNumber: this.profileForm.phoneNumber,
      title: this.profileForm.title,
      hourlyRate: Number(this.profileForm.hourlyRate) || 0,
      summary: this.profileForm.summary,
      bio: this.profileForm.bio,
      location: this.profileForm.location,
      profilePicture: this.profileForm.profilePicture,
      experienceLevel: this.profileForm.experienceLevel
    } as Partial<BackendFreelancer>;

    console.log('Saving profile with payload:', payload);

    this.freelancerService.updateFreelancer(this.freelancerRaw.id, payload).subscribe({
      next: (updated) => {
        console.log('Profile saved successfully:', updated);
        this.freelancerRaw = updated;
        this.profileSaved = true;
        this.profileError = null;
        this.profileLoading = false;
        // Reload profile to get fresh data
        setTimeout(() => {
          this.profileSaved = false;
        }, 3000);
      },
      error: (err) => {
        console.error('Error saving profile:', err);
        console.error('Error status:', err.status);
        console.error('Error details:', err.error);
        this.profileError = 'Could not save changes: ' + (err.error?.error || err.message);
        this.profileLoading = false;
      }
    });
  }

  loadActiveMissions(): void {
    const userId = this.authService.getUserId();
    console.log('Loading active missions for userId:', userId);
    
    if (!userId) {
      this.missionsError = 'User ID not found';
      this.missionsLoading = false;
      return;
    }

    this.missionsLoading = true;
    this.missionsError = null;

    this.freelancerService.getFreelancerActiveMissions(userId).subscribe({
      next: (response: any) => {
        console.log('Received active missions response:', response);
        
        if (response.success === false) {
          console.warn('API returned error:', response.error);
          this.missionsError = response.error || 'Failed to load missions';
          this.activeMissions = [];
        } else if (response.activeMissions && Array.isArray(response.activeMissions)) {
          this.activeMissions = response.activeMissions.map((mission: any) => ({
            id: mission.id,
            applicationId: mission.applicationId,
            title: mission.title,
            budget: mission.budget,
            status: mission.status || 'In progress',
            recruiter: mission.recruiterName,
            acceptedDate: mission.acceptedDate,
            description: mission.description,
            type: mission.type,
            deadline: mission.deadline
          }));
          console.log('Transformed active missions:', this.activeMissions);
          this.stats[0].value = this.activeMissions.length;
          this.stats[0].trend = `${this.activeMissions.length} ongoing`;
          this.missionsError = null;
        } else {
          this.activeMissions = [];
          this.missionsError = null;
        }
        this.missionsLoading = false;
      },
      error: (err) => {
        console.error('Error loading active missions:', err);
        console.error('Error status:', err.status);
        console.error('Error message:', err.message);
        
        // Provide more specific error messages
        if (err.status === 0) {
          this.missionsError = 'Cannot connect to server';
        } else if (err.status === 404) {
          this.missionsError = 'API endpoint not found';
        } else if (err.status === 401) {
          this.missionsError = 'Unauthorized - please log in again';
        } else {
          this.missionsError = err.error?.error || 'Failed to load missions';
        }
        
        this.missionsLoading = false;
        this.activeMissions = [];
      }
    });
  }

  private formatDueDate(dateString: string): string {
    if (!dateString) return 'No deadline';
    const date = new Date(dateString);
    const daysFromNow = Math.floor((date.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
    if (daysFromNow < 0) return 'Overdue';
    if (daysFromNow === 0) return 'Due today';
    if (daysFromNow === 1) return 'Due tomorrow';
    return `Due in ${daysFromNow} days`;
  }

  setTab(tab: typeof this.activeTab): void {
    this.activeTab = tab;
    // Refresh data when switching tabs
    if (tab === 'proposals') {
      this.loadProposals();
    } else if (tab === 'missions') {
      this.loadActiveMissions();
    } else if (tab === 'offers') {
      this.loadOffers();
    } else if (tab === 'profile') {
      this.loadPortfolios();
    }
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

  private formatTimeLabel(date: Date): string {
    return date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
  }

  getLastMessage(thread: ConversationThread): ChatMessage | null {
    if (!thread.messages.length) return null;
    return thread.messages[thread.messages.length - 1];
  }

  sendMessage(): void {
    const thread = this.selectedThread;
    const text = this.draftMessage.trim();
    if (!thread || !text) return;

    const now = new Date();
    const updatedMessage: ChatMessage = {
      id: Date.now(),
      from: 'me',
      text,
      time: this.formatTimeLabel(now)
    };

    this.messageThreads = this.messageThreads.map(t =>
      t.id === thread.id ? { ...t, messages: [...t.messages, updatedMessage] } : t
    );
    this.draftMessage = '';
  }

  loadOffers(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    
    this.offersLoading = true;
    this.offersError = null;
    
    this.http.get<any[]>('http://localhost:8090/api/contracts/freelancer', { headers }).subscribe({
      next: (contracts) => {
        this.offers = contracts.map(contract => ({
          id: contract.id,
          title: contract.title,
          description: contract.description,
          budget: contract.budget,
          status: contract.status,
          recruiterName: `${contract.recruiter?.firstName || ''} ${contract.recruiter?.lastName || ''}`.trim(),
          recruiterCompany: contract.recruiter?.companyName || 'Company',
          createdAt: contract.createdAt || new Date().toISOString()
        }));
        this.offersLoading = false;
      },
      error: (err) => {
        console.error('Error loading offers:', err);
        this.offersError = 'Failed to load offers';
        this.offersLoading = false;
      }
    });
  }

  acceptOffer(offerId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    
    this.http.put<any>(
      `http://localhost:8090/api/contracts/${offerId}/status`,
      { status: 'ACCEPTED' },
      { headers }
    ).subscribe({
      next: () => {
        alert('Offer accepted successfully!');
        this.offers = this.offers.filter(o => o.id !== offerId);
        this.loadOffers();
      },
      error: (err) => {
        console.error('Error accepting offer:', err);
        alert('Failed to accept offer');
      }
    });
  }

  rejectOffer(offerId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    
    this.http.put<any>(
      `http://localhost:8090/api/contracts/${offerId}/status`,
      { status: 'REJECTED' },
      { headers }
    ).subscribe({
      next: () => {
        alert('Offer rejected');
        this.offers = this.offers.filter(o => o.id !== offerId);
        this.loadOffers();
      },
      error: (err) => {
        console.error('Error rejecting offer:', err);
        alert('Failed to reject offer');
      }
    });
  }

  loadProposals(): void {
    const userId = this.authService.getUserId();
    console.log('Loading proposals for userId:', userId);
    
    if (!userId) {
      this.proposalsError = 'User ID not found';
      console.error('User ID not found');
      return;
    }

    this.proposalsLoading = true;
    this.proposalsError = null;

    this.freelancerService.getFreelancerApplications(userId).subscribe({
      next: (applications: any[]) => {
        console.log('Received applications:', applications);
        this.proposals = applications.map(app => ({
          id: app.id,
          mission: app.mission?.title || 'Unknown Mission',
          submitted: this.formatTimeAgo(app.appliedDate),
          status: this.formatStatus(app.status),
          appliedDate: app.appliedDate,
          coverLetter: app.coverLetter,
          proposedBudget: app.proposedBudget
        })).sort((a, b) => new Date(b.appliedDate).getTime() - new Date(a.appliedDate).getTime());
        
        console.log('Transformed proposals:', this.proposals);
        this.stats[1].value = this.proposals.length;
        this.stats[1].trend = `${this.proposals.filter(p => p.status === 'Pending').length} pending`;
        this.proposalsLoading = false;
      },
      error: (err) => {
        console.error('Error loading proposals:', err);
        this.proposalsError = 'Failed to load proposals';
        this.proposalsLoading = false;
        // Fall back to empty array on error
        this.proposals = [];
      }
    });
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

  private formatStatus(status: string): string {
    if (!status) return 'Pending';
    
    const statusMap: { [key: string]: string } = {
      'PENDING': 'Pending',
      'VIEWED': 'Viewed',
      'ACCEPTED': 'Accepted',
      'REJECTED': 'Declined',
      'DECLINED': 'Declined',
      'COMPLETED': 'Completed'
    };

    return statusMap[status.toUpperCase()] || status;
  }
}
