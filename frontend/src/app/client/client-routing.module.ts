import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductCatalogComponent } from './components/product-catalog/product-catalog.component';
import { DirectPurchaseComponent } from './components/direct-purchase/direct-purchase.component';

const routes: Routes = [
  { path: 'products', component: ProductCatalogComponent },
  { path: 'buy/:id', component: DirectPurchaseComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }