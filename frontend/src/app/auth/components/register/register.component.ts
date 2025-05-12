import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  user = {
    nome: '',
    sobrenome: '',
    idade: null,
    cidade: '',
    email: '',
    telefone: '',
    senha: ''
  };
  successMessage = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  register() {
    this.authService.register(this.user).subscribe({
      next: (response) => {
        this.successMessage = 'Cadastro realizado com sucesso! Redirecionando para o login...';
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
      },
      error: (error) => {
        this.errorMessage = 'Erro ao cadastrar usuário.';
        console.error('Erro de registro:', error);
      }
    });
  }
}