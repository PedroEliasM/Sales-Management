import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';
import { Product } from '../../core/models/product.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private apiService: ApiService) { }

  getProducts(): Observable<Product[]> {
    return this.apiService.get<Product[]>('/produtos');
  }

  getProduct(id: number): Observable<Product> {
    return this.apiService.get<Product>(`/produtos/${id}`);
  }

  addProduct(product: Product): Observable<Product> {
    return this.apiService.post<Product>('/produtos', product);
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.apiService.put<Product>(`/produtos/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.apiService.delete<void>(`/produtos/${id}`);
  }
}