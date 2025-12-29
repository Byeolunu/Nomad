import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-moroccan-pattern',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './moroccan-pattern.html',
  styleUrls: ['./moroccan-pattern.css']
})
export class MoroccanPatternComponent {
  @Input() className = '';
}

