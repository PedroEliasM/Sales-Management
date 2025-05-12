import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;
  isEditMode = false;
  productId: number | null = null;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.productForm = this.fb.group({
      nome: ['', Validators.required],
      valor: [null, [Validators.required, Validators.min(0.01)]],
      descricao: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.productId = params['id'];
      if (this.productId) {
        this.isEditMode = true;
        this.loadProductDetails(this.productId);
      }
    });
  }

  loadProductDetails(id: number) {
    this.employeeService.getProduct(id).subscribe({
      next: (product) => {
        this.productForm.patchValue(product);
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar detalhes do produto.';
        console.error('Erro:', error);
      }
    });
  }

  onSubmit() {
    if (this.productForm.valid) {
      const productData: Product = this.productForm.value;
      if (this.isEditMode && this.productId) {
        this.employeeService.updateProduct(this.productId, productData).subscribe({
          next: () => {
            this.router.navigate(['/employee/products']);
          },
          error: (error) => {
            this.errorMessage = 'Erro ao atualizar produto.';
            console.error('Erro:', error);
          }
        });
      } else {
        this.employeeService.addProduct(productData).subscribe({
          next: () => {
            this.router.navigate(['/employee/products']);
          },
          error: (error) => {
            this.errorMessage = 'Erro ao adicionar produto.';
            console.error('Erro:', error);
          }
        });
      }
    }
  }
}