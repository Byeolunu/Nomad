import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Skill {
  id: number;
  name: string;
}

export interface Recruiter {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  companyName: string;
  companyWebsite: string;
}

export interface Mission {
  id: number;
  title: string;
  description: string;
  status: string;
  budget: number;
  createdAt: string;
  deadline: string;
  type: string;
  recruiter: Recruiter;
  requiredSkills: Skill[];
  applicationCount: number;
}

export interface MissionDisplay {
  id: string;
  title: string;
  company: string;
  companyLogo: string;
  description: string;
  category: string;
  skills: string[];
  budget: string;
  location: string;
  experienceLevel: string;
  applicants: number;
  postedDate: string;
  recruiter?: Recruiter;
}

@Injectable({
  providedIn: 'root'
})
export class MissionService {
  private apiUrl = 'http://localhost:8090/api/missions';

  constructor(private http: HttpClient) {}

  getAllMissions(): Observable<MissionDisplay[]> {
    return this.http.get<Mission[]>(this.apiUrl).pipe(
      map(missions => missions.map(m => this.transformMission(m)))
    );
  }

  getMissionById(id: string): Observable<MissionDisplay> {
    return this.http.get<Mission>(`${this.apiUrl}/${id}`).pipe(
      map(mission => this.transformMission(mission))
    );
  }

  getMissionsByRecruiter(recruiterId: number): Observable<MissionDisplay[]> {
    return this.http.get<Mission[]>(`${this.apiUrl}/recruiter/${recruiterId}`).pipe(
      map(missions => missions.map(m => this.transformMission(m)))
    );
  }

  private transformMission(mission: Mission): MissionDisplay {
    const companyName = mission.recruiter?.companyName || 'Unknown Company';
    const companyLogo = this.getCompanyInitials(companyName);
    const postedDate = this.formatDate(mission.createdAt);
    const skills = mission.requiredSkills?.map(s => s.name) || [];
    const category = this.inferCategory(skills, mission.title, mission.description);
    const experienceLevel = this.inferExperienceLevel(mission.budget);

    return {
      id: mission.id.toString(),
      title: mission.title,
      company: companyName,
      companyLogo: companyLogo,
      description: mission.description || '',
      category: category,
      skills: skills.length > 0 ? skills : this.extractSkillsFromDescription(mission.description),
      budget: this.formatBudget(mission.budget),
      location: 'Morocco',
      experienceLevel: experienceLevel,
      applicants: mission.applicationCount || 0,
      postedDate: postedDate,
      recruiter: mission.recruiter
    };
  }

  private getCompanyInitials(companyName: string): string {
    if (!companyName) return '??';
    const words = companyName.split(' ');
    if (words.length >= 2) {
      return (words[0][0] + words[1][0]).toUpperCase();
    }
    return companyName.substring(0, 2).toUpperCase();
  }

  private formatDate(dateString: string): string {
    if (!dateString) return 'Recently';
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 0) return 'Today';
    if (diffDays === 1) return '1 day ago';
    if (diffDays < 7) return `${diffDays} days ago`;
    if (diffDays < 14) return '1 week ago';
    if (diffDays < 30) return `${Math.floor(diffDays / 7)} weeks ago`;
    return `${Math.floor(diffDays / 30)} month(s) ago`;
  }

  private formatBudget(budget: number | null): string {
    if (!budget) return 'Negotiable';
    return `${budget.toLocaleString()} MAD`;
  }

  private inferCategory(skills: string[], title: string, description: string): string {
    const text = (title + ' ' + description + ' ' + skills.join(' ')).toLowerCase();
    
    if (text.includes('react') || text.includes('angular') || text.includes('node') || 
        text.includes('java') || text.includes('python') || text.includes('developer') ||
        text.includes('backend') || text.includes('frontend') || text.includes('full-stack')) {
      return 'Development';
    }
    if(text.includes('machine learning') || text.includes('data science') || text.includes('ai') ||
       text.includes('deep learning')) {
      return 'Data Science';
    }

    if (text.includes('design') || text.includes('ui') || text.includes('ux') || 
        text.includes('figma') || text.includes('photoshop')) {
      return 'Design';
    }
    if (text.includes('marketing') || text.includes('seo') || text.includes('social media')) {
      return 'Marketing';
    }
    if (text.includes('video') || text.includes('animation') || text.includes('motion')) {
      return 'Video';
    }
    if (text.includes('writing') || text.includes('content') || text.includes('copywriting')) {
      return 'Writing';
    }
    if (text.includes('translation') || text.includes('translator')) {
      return 'Translation';
    }
    return 'Development';
    if (text.includes('machine learning') || text.includes('data science') || text.includes('ai') ||
       text.includes('deep learning')) {
      return 'Data Science';
    }
  }

  private inferExperienceLevel(budget: number | null): string {
    if (!budget) return 'Entry';
    if (budget < 2000) return 'Entry';
    if (budget < 5000) return 'Intermediate';
    return 'Expert';
  }

  private extractSkillsFromDescription(description: string): string[] {
    if (!description) return ['General'];
    const commonSkills = ['React', 'Angular', 'Node.js', 'Python', 'Java', 'Spring Boot', 
                          'JavaScript', 'TypeScript','Machine Learning', 'HTML', 'CSS', 'SQL', 'MongoDB','Deep Learning',
                          'Figma', 'UI/UX', 'Design', 'Marketing', 'SEO','AI'];
    const found = commonSkills.filter(skill => 
      description.toLowerCase().includes(skill.toLowerCase())
    );
    return found.length > 0 ? found : ['General'];
  }
}
