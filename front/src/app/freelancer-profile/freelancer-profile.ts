import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FreelancerService, FreelancerDisplay } from '../services/freelancer.service';
import { ReviewService, CanReviewResponse } from '../services/review.service';
import { AuthService } from '../auth/auth';

@Component({
  selector: 'app-freelancer-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './freelancer-profile.html',
  styleUrl: './freelancer-profile.css'
})
export class FreelancerProfileComponent implements OnInit {
  freelancer: FreelancerDisplay | null = null;
  isLoading = true;
  error: string | null = null;
  isLoggedIn = false;
  isRecruiter = false;

  canReview = false;
  hasReviewed = false;
  hasWorkedTogether = false;
  existingReviewId: number | null = null;
  showReviewForm = false;
  isEditMode = false;
  reviewRating = 5;
  reviewComment = '';
  isSubmittingReview = false;
  reviewError: string | null = null;
  reviewSuccess: string | null = null;
  showDeleteConfirm = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private freelancerService: FreelancerService,
    private reviewService: ReviewService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedin();
    this.isRecruiter = this.authService.isRecruiter();

    this.route.params.subscribe(params => {
      const id = params['id'];
      this.loadFreelancer(id);
      if (this.isLoggedIn && this.isRecruiter) {
        this.checkCanReview(id);
      }
    });
  }

  checkCanReview(freelancerId: string): void {
    this.reviewService.canReviewFreelancer(parseInt(freelancerId)).subscribe({
      next: (response: CanReviewResponse) => {
        this.canReview = response.canReview;
        this.hasReviewed = response.hasReviewed;
        this.hasWorkedTogether = response.hasWorkedTogether;
        this.existingReviewId = response.existingReviewId;
      },
      error: (err) => {
        console.error('Error checking review status:', err);
      }
    });
  }

  loadFreelancer(id: string): void {
    this.isLoading = true;
    this.error = null;

    this.freelancerService.getFreelancerById(id).subscribe({
      next: (freelancer) => {
        this.freelancer = freelancer;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading freelancer:', err);
        this.error = 'Failed to load freelancer profile.';
        this.isLoading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/freelancers']);
  }

  hireFreelancer(): void {
    if (this.isLoggedIn && this.isRecruiter) {
      this.router.navigate(['/hire', this.freelancer?.id]);
    } else {
      this.router.navigate(['/signup']);
    }
  }

  getStars(count: number): number[] {
    return Array(count).fill(0);
  }

  toggleReviewForm(): void {
    this.showReviewForm = !this.showReviewForm;
    this.isEditMode = false;
    this.reviewError = null;
    this.reviewSuccess = null;
    if (!this.showReviewForm) {
      this.reviewComment = '';
      this.reviewRating = 5;
    }
  }

  editReview(review: { id: string; rating: number; comment: string }): void {
    this.isEditMode = true;
    this.showReviewForm = true;
    this.reviewRating = review.rating;
    this.reviewComment = review.comment;
    this.reviewError = null;
    this.reviewSuccess = null;
  }

  startEditReview(): void {
    if (this.freelancer && this.existingReviewId) {
      const reviewId = this.existingReviewId.toString();
      const myReview = this.freelancer.reviews.find(r => r.id === reviewId);
      if (myReview) {
        this.editReview(myReview);
      }
    }
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.showReviewForm = false;
    this.reviewComment = '';
    this.reviewRating = 5;
  }

  confirmDelete(): void {
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
  }

  deleteReview(): void {
    if (!this.existingReviewId) return;

    this.reviewService.deleteReview(this.existingReviewId).subscribe({
      next: () => {
        this.showDeleteConfirm = false;
        this.hasReviewed = false;
        this.canReview = true;
        this.existingReviewId = null;
        this.loadFreelancer(this.freelancer!.id);
      },
      error: (err) => {
        this.reviewError = err.error?.error || 'Failed to delete review';
        this.showDeleteConfirm = false;
      }
    });
  }

  setRating(rating: number): void {
    this.reviewRating = rating;
  }

  submitReview(): void {
    if (!this.freelancer || this.isSubmittingReview) return;

    if (!this.reviewComment.trim()) {
      this.reviewError = 'Please write a comment';
      return;
    }

    this.isSubmittingReview = true;
    this.reviewError = null;

    const reviewOperation = this.isEditMode && this.existingReviewId
      ? this.reviewService.updateReview(this.existingReviewId, this.reviewRating, this.reviewComment.trim())
      : this.reviewService.createReview(parseInt(this.freelancer.id), this.reviewRating, this.reviewComment.trim());

    reviewOperation.subscribe({
      next: (response) => {
        this.reviewSuccess = this.isEditMode ? 'Review updated successfully!' : 'Review submitted successfully!';
        this.isSubmittingReview = false;
        this.showReviewForm = false;
        this.hasReviewed = true;
        this.canReview = false;
        this.isEditMode = false;
        this.existingReviewId = response.id;
        this.reviewComment = '';
        this.reviewRating = 5;
        // Reload freelancer to show updated review
        this.loadFreelancer(this.freelancer!.id);
      },
      error: (err) => {
        this.reviewError = err.error?.error || 'Failed to submit review. Please try again.';
        this.isSubmittingReview = false;
      }
    });
  }
}
