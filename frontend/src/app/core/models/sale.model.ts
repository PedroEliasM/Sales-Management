export interface Sale {
  id?: number;
  idCliente: number;
  valorTotal?: number;
  itens: SaleItem[];
}

export interface SaleItem {
  id?: number;
  idProduto: number;
  quantidade: number;
  valorUnitario?: number;
  valorTotal?: number;
  nomeProduto?: string;
}