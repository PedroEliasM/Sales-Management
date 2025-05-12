import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  credentials = {
    email: '',
    senha: ''
  };
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  login() {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userType', response.tipoUsuario);
        localStorage.setItem('userId', response.userId.toString());
        localStorage.setItem('userName', response.nome);
        this.router.navigate(['/']); // Redirecionar para a página inicial após o login
      },
      error: (error) => {
        this.errorMessage = 'Credenciais inválidas.';
        console.error('Erro de login:', error);
      }
    });
  }
}