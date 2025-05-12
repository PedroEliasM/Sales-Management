import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/client.service';
import { Product } from '../../../core/models/product.model';
import { Router } from '@angular/router'; // Importar o Router

@Component({
  selector: 'app-product-catalog',
  templateUrl: './product-catalog.component.html',
  styleUrls: ['./product-catalog.component.scss']
})
export class ProductCatalogComponent implements OnInit {
  products: Product[] = [];
  errorMessage = '';

  constructor(private clientService: ClientService, private router: Router) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.clientService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar produtos.';
        console.error('Erro:', error);
      }
    });
  }

  buyNow(product: Product) {
    // Redirecionar para um componente de compra direta, passando o produto como parâmetro
    this.router.navigate(['/client/buy', product.id]);
    // Ou, se a compra for imediata, chamar um serviço para iniciar a compra
    // this.clientService.buyProduct(product.id).subscribe(...);
  }
}