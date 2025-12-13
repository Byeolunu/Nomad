import { Routes } from '@angular/router';
import { LoginComponent } from '../auth/login/login';
import { RegisterComponent } from '../auth/register/register';

export const routes: Routes = [
	{ path: '', redirectTo: 'login', pathMatch: 'full' },
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },
	// placeholder: missions list route to be implemented
	{ path: 'missions', component: LoginComponent } // temporary route target
];
