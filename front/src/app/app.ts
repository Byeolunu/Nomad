import { Component, signal } from '@angular/core';
import { Header } from './components/header/header';
import { Footer } from './components/footer/footer';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [Header, Footer, RouterOutlet],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('front');
}
