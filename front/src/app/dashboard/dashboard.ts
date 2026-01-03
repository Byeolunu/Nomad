import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth';
import { FreelancerService, BackendFreelancer } from '../services/freelancer.service';

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
  activeTab: 'overview' | 'missions' | 'proposals' | 'messages' | 'profile' = 'overview';
  userName: string = '';
  readonlyName: string = '';
  readonlyEmail: string = '';
  profileLoading = false;
  profileError: string | null = null;
  profileSaved = false;
  profileForm = {
    phoneNumber: '',
    title: '',
    hourlyRate: 0,
    summary: ''
  };
  freelancerRaw: BackendFreelancer | null = null;

  stats = [
    { label: 'Active missions', value: 2, trend: '+1 this week' },
    { label: 'Proposals sent', value: 5, trend: '2 pending' },
    { label: 'Invites', value: 1, trend: 'New today' },
    { label: 'Messages', value: 3, trend: 'Unread' }
  ];

  activeMissions = [
    { title: 'UI revamp for marketplace', budget: '18,000 MAD', status: 'In progress', due: 'Due in 6 days', progress: 65 },
    { title: 'Mobile onboarding flow', budget: '12,500 MAD', status: 'Awaiting assets', due: 'Due in 14 days', progress: 35 }
  ];

  proposals = [
    { mission: 'Data viz dashboard', submitted: '2d ago', status: 'Pending' },
    { mission: 'Payments integration', submitted: '5d ago', status: 'Viewed' },
    { mission: 'Marketing site refresh', submitted: '1w ago', status: 'Declined' }
  ];

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
    private freelancerService: FreelancerService
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
  }

  getUserName(): string {
    return this.userEmail.split('@')[0] || 'Freelancer';
  }

  getInitials(): string {
    const name = this.getUserName();
    if (!name) return 'F';
    const parts = name.split(/[._-]/).filter(Boolean);
    if (parts.length >= 2) {
      return `${parts[0].charAt(0)}${parts[1].charAt(0)}`.toUpperCase();
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
      this.profileForm.phoneNumber = freelancer.phoneNumber || '';
      this.profileForm.title = freelancer.title || '';
      this.profileForm.hourlyRate = freelancer.hourlyRate || 0;
      this.profileForm.summary = freelancer.summary || freelancer.profile?.bio || '';
      this.readonlyName = `${freelancer.firstName || ''} ${freelancer.lastName || ''}`.trim() || this.userName;
      this.readonlyEmail = freelancer.email || this.userEmail;
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

  saveProfile(): void {
    if (!this.freelancerRaw) return;
    this.profileLoading = true;
    this.profileSaved = false;
    const payload = {
      phoneNumber: this.profileForm.phoneNumber,
      title: this.profileForm.title,
      hourlyRate: Number(this.profileForm.hourlyRate) || 0,
      summary: this.profileForm.summary
    } as Partial<BackendFreelancer>;

    this.freelancerService.updateFreelancer(this.freelancerRaw.id, payload).subscribe({
      next: (updated) => {
        this.freelancerRaw = updated;
        this.profileSaved = true;
        this.profileError = null;
        this.profileLoading = false;
      },
      error: (err) => {
        console.error('Error saving profile', err);
        this.profileError = 'Could not save changes';
        this.profileLoading = false;
      }
    });
  }

  setTab(tab: typeof this.activeTab): void {
    this.activeTab = tab;
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
      t.id === thread.id ? { ...t, messages: [...t.messages, updatedMessage], unread: false } : t
    );

    this.draftMessage = '';
  }
}
