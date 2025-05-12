import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  errorMessage = '';

  constructor(private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.employeeService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar produtos.';
        console.error('Erro:', error);
      }
    });
  }

  deleteProduct(id: number) {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
      this.employeeService.deleteProduct(id).subscribe({
        next: () => {
          this.loadProducts(); // Recarregar a lista após a exclusão
        },
        error: (error) => {
          this.errorMessage = 'Erro ao excluir produto.';
          console.error('Erro:', error);
        }
      });
    }
  }
}