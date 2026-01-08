import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface CompanyData {
  name: string;
  website: string;
  size: string;
  location: string;
  blurb: string;
  recruiter?: {
    firstName: string;
    lastName: string;
    email: string;
    companyName: string;
    companyWebsite: string;
  };
}

@Component({
  selector: 'app-company-profile',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="company-profile-page">
      <div class="company-container">
        <button class="back-btn font-maglite" (click)="goBack()">
          <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
          </svg>
          Back
        </button>

        @if (isLoading) {
          <div class="loading-card">
            <p class="font-hermione">Loading company profile...</p>
          </div>
        } @else if (error) {
          <div class="error-card">
            <p class="font-hermione">{{ error }}</p>
          </div>
        } @else if (company) {
          <div class="company-card">
            <div class="company-header">
              <div class="company-logo">
                <span class="font-maglite">{{ getCompanyInitials() }}</span>
              </div>
              <div class="company-title-section">
                <h1 class="company-name font-maglite">{{ company.name }}</h1>
                <p class="company-location font-hermione">{{ company.location }}</p>
              </div>
            </div>

            <div class="company-details">
              <div class="detail-section">
                <h2 class="section-title font-maglite">About the Company</h2>
                <p class="description font-hermione">{{ company.blurb }}</p>
              </div>

              <div class="detail-grid">
                <div class="detail-item">
                  <h3 class="detail-label font-hermione">Company Size</h3>
                  <p class="detail-value font-maglite">{{ company.size }}</p>
                </div>
                <div class="detail-item">
                  <h3 class="detail-label font-hermione">Location</h3>
                  <p class="detail-value font-maglite">{{ company.location }}</p>
                </div>
              </div>

              @if (company.website) {
                <div class="detail-section">
                  <h3 class="section-title font-maglite">Website</h3>
                  <a [href]="company.website" target="_blank" class="website-link font-hermione">
                    {{ company.website }}
                  </a>
                </div>
              }

              @if (company.recruiter) {
                <div class="detail-section">
                  <h2 class="section-title font-maglite">Contact Person</h2>
                  <div class="recruiter-info">
                    <div class="recruiter-item">
                      <span class="label font-hermione">Name:</span>
                      <span class="value font-maglite">{{ company.recruiter.firstName }} {{ company.recruiter.lastName }}</span>
                    </div>
                    <div class="recruiter-item">
                      <span class="label font-hermione">Email:</span>
                      <a [href]="'mailto:' + company.recruiter.email" class="value font-maglite">{{ company.recruiter.email }}</a>
                    </div>
                  </div>
                </div>
              }
            </div>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
    }

    .company-profile-page {
      min-height: 100vh;
      background: linear-gradient(135deg, #faf9f6 0%, #f5f3ef 100%);
      padding: 2rem 1rem;
    }

    .company-container {
      max-width: 900px;
      margin: 0 auto;
    }

    .back-btn {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.875rem 1.25rem;
      margin-bottom: 2rem;
      background: white;
      border: 1px solid rgba(112, 5, 13, 0.08);
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.2s ease;
      font-weight: 500;
      color: #57011A;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    }

    .back-btn:hover {
      background: rgba(112, 5, 13, 0.02);
      border-color: rgba(112, 5, 13, 0.12);
      transform: translateX(-2px);
    }

    .back-btn svg {
      width: 20px;
      height: 20px;
      stroke-width: 2;
    }

    .loading-card, .error-card {
      background: white;
      padding: 3rem;
      border-radius: 16px;
      text-align: center;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
      border: 1px solid rgba(112, 5, 13, 0.04);
    }

    .error-card {
      color: #d32f2f;
      border-color: rgba(211, 47, 47, 0.2);
      background: rgba(211, 47, 47, 0.02);
    }

    .company-card {
      background: white;
      border-radius: 16px;
      overflow: hidden;
      box-shadow: 0 6px 24px rgba(112, 5, 13, 0.08);
      border: 1px solid rgba(112, 5, 13, 0.04);
    }

    .company-header {
      display: flex;
      align-items: center;
      gap: 2.5rem;
      padding: 3rem;
      background: linear-gradient(135deg, #6B7C3A 0%, #5A6B32 100%);
      color: white;
    }

    .company-logo {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 120px;
      height: 120px;
      background: rgba(255, 255, 255, 0.15);
      border: 2px solid rgba(255, 255, 255, 0.25);
      border-radius: 16px;
      font-size: 3rem;
      font-weight: 700;
      flex-shrink: 0;
    }

    .company-title-section {
      flex: 1;
    }

    .company-name {
      font-size: 2.25rem;
      margin: 0 0 0.5rem 0;
      color: white;
      font-weight: 700;
      letter-spacing: -0.01em;
    }

    .company-location {
      margin: 0;
      opacity: 0.95;
      font-size: 1rem;
    }

    .company-details {
      padding: 3rem;
    }

    .detail-section {
      margin-bottom: 2.5rem;
    }

    .section-title {
      font-size: 1.25rem;
      margin: 0 0 1.25rem 0;
      color: #57011A;
      font-weight: 600;
      letter-spacing: -0.01em;
    }

    .description {
      line-height: 1.7;
      color: #555;
      margin: 0;
      font-size: 1rem;
    }

    .detail-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }

    .detail-item {
      padding: 1.75rem;
      background: linear-gradient(135deg, #f9f9f6 0%, #f5f3ef 100%);
      border: 1px solid rgba(112, 5, 13, 0.05);
      border-radius: 12px;
      transition: all 0.2s ease;
    }

    .detail-item:hover {
      border-color: rgba(107, 124, 58, 0.2);
      background: linear-gradient(135deg, #fafaf8 0%, #f8f6f3 100%);
    }

    .detail-label {
      font-size: 0.75rem;
      color: #8B8B8B;
      margin: 0 0 0.75rem 0;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      font-weight: 600;
    }

    .detail-value {
      font-size: 1.125rem;
      color: #70050D;
      margin: 0;
      font-weight: 600;
    }

    .website-link {
      display: inline-block;
      color: #6B7C3A;
      text-decoration: none;
      word-break: break-all;
      transition: all 0.2s ease;
      font-weight: 500;
      padding: 0.5rem 0;
      border-bottom: 2px solid transparent;
    }

    .website-link:hover {
      color: #5A6B32;
      border-bottom-color: #6B7C3A;
    }

    .recruiter-info {
      display: flex;
      flex-direction: column;
      gap: 1.25rem;
      padding: 2rem;
      background: linear-gradient(135deg, #f9f9f6 0%, #f5f3ef 100%);
      border: 1px solid rgba(107, 124, 58, 0.15);
      border-radius: 12px;
    }

    .recruiter-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 1rem;
      border-bottom: 1px solid rgba(112, 5, 13, 0.05);
    }

    .recruiter-item:last-child {
      border-bottom: none;
      padding-bottom: 0;
    }

    .recruiter-item .label {
      color: #666;
      font-weight: 600;
      font-size: 0.95rem;
    }

    .recruiter-item .value {
      color: #57011A;
      text-decoration: none;
      font-weight: 500;
    }

    .recruiter-item a {
      color: #6B7C3A;
      text-decoration: none;
      transition: all 0.2s ease;
      font-weight: 500;
      border-bottom: 1px solid transparent;
    }

    .recruiter-item a:hover {
      color: #5A6B32;
      border-bottom-color: #6B7C3A;
    }

    @media (max-width: 768px) {
      .company-header {
        flex-direction: column;
        text-align: center;
        padding: 2rem;
      }

      .company-name {
        font-size: 1.75rem;
      }

      .company-logo {
        width: 100px;
        height: 100px;
        font-size: 2.5rem;
      }

      .detail-grid {
        grid-template-columns: 1fr;
      }

      .recruiter-item {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.5rem;
      }

      .company-details {
        padding: 1.5rem;
      }
    }
  `]
})
export class CompanyProfileComponent implements OnInit {
  company: CompanyData | null = null;
  isLoading = true;
  error: string | null = null;
  private recruiterId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.recruiterId = params['id'];
      this.loadCompanyProfile();
    });
  }

  loadCompanyProfile() {
    if (!this.recruiterId) {
      this.error = 'Company not found';
      this.isLoading = false;
      return;
    }

    this.isLoading = true;
    // Fetch recruiter data to get company info
    this.http.get<any>(`http://localhost:8090/api/users/${this.recruiterId}`).subscribe({
      next: (data) => {
        this.company = {
          name: data.companyName || 'Company',
          website: data.companyWebsite || '',
          size: '51-200 employees',
          location: 'Morocco',
          blurb: data.summary || 'A company dedicated to excellence',
          recruiter: {
            firstName: data.firstName || '',
            lastName: data.lastName || '',
            email: data.email || '',
            companyName: data.companyName || '',
            companyWebsite: data.companyWebsite || ''
          }
        };
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading company profile:', err);
        this.error = 'Failed to load company profile';
        this.isLoading = false;
      }
    });
  }

  getCompanyInitials(): string {
    if (!this.company) return 'CO';
    const words = this.company.name.split(' ');
    return words.map(w => w[0]).join('').toUpperCase().substring(0, 2);
  }

  goBack(): void {
    this.router.navigate(['/missions']);
  }
}
