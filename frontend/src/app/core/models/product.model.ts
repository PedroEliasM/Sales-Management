export interface Product {
  id?: number;
  nome: string;
  valor: number;
  descricao?: string;
  idFuncionarioCadastro?: number;
  quantidadeEstoque?: number;
}