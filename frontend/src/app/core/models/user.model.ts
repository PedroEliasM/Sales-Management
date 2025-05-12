export interface User {
  id?: number;
  nome: string;
  sobrenome: string;
  idade: number;
  cidade: string;
  tipoUsuario: string;
  email: string;
  telefone?: string;
  senha?: string;
}