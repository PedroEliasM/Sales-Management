import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';
import { Product } from '../../core/models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private apiService: ApiService) { }

  getProducts(): Observable<Product[]> {
    return this.apiService.get<Product[]>('/produtos');
  }

  getProduct(id: number): Observable<Product> {
    return this.apiService.get<Product>(`/produtos/${id}`);
  }

  buyProduct(purchaseDetails: { productId: number | undefined; quantity: number }): Observable<any> {
    return this.apiService.post<any>('/vendas', { idProduto: purchaseDetails.productId, quantidade: purchaseDetails.quantity });
  }
}