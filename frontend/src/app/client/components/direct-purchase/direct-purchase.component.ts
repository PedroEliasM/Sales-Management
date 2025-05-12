import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-direct-purchase',
  templateUrl: './direct-purchase.component.html',
  styleUrls: ['./direct-purchase.component.scss']
})
export class DirectPurchaseComponent implements OnInit {
  productId: number | null = null;
  product: Product | null = null;
  quantity: number = 1;
  errorMessage = '';

  constructor(private route: ActivatedRoute, private clientService: ClientService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.productId = +params['id']; // Converter para número
      if (this.productId) {
        this.loadProductDetails(this.productId);
      }
    });
  }

  loadProductDetails(id: number) {
    this.clientService.getProduct(id).subscribe({
      next: (data) => {
        this.product = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar detalhes do produto.';
        console.error('Erro:', error);
      }
    });
  }

  purchase() {
    if (this.product) {
      const purchaseDetails = {
        productId: this.product.id,
        quantity: this.quantity
      };
      this.clientService.buyProduct(purchaseDetails).subscribe({
        next: (response) => {
          console.log('Compra realizada com sucesso:', response);
          // Redirecionar para uma página de confirmação
        },
        error: (error) => {
          this.errorMessage = 'Erro ao realizar a compra.';
          console.error('Erro:', error);
        }
      });
    }
  }
}