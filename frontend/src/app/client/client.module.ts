import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientRoutingModule } from './client-routing.module';
import { ProductCatalogComponent } from './components/product-catalog/product-catalog.component';
import { ClientService } from './services/client.service';
import { DirectPurchaseComponent } from './components/direct-purchase/direct-purchase.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ProductCatalogComponent,
    DirectPurchaseComponent
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    FormsModule
  ],
  providers: [
    ClientService
  ]
})
export class ClientModule { }