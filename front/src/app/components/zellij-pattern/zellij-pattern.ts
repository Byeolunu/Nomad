import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-zellij-pattern',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './zellij-pattern.html',
  styleUrls: ['./zellij-pattern.css']
})
export class ZellijPatternComponent {
  @Input() className = '';
  @Input() opacity = 0.05;
}

