import { Component, signal } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import { FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {LoginComponent} from '../../auth/login/login';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink, LoginComponent],
  templateUrl: './header.html'
})
export class Header {
  protected readonly title = signal('front');
}

