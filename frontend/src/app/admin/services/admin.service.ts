import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';
import { Employee } from '../../core/models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private apiService: ApiService) { }

  getEmployees(): Observable<Employee[]> {
    return this.apiService.get<Employee[]>('/usuarios/admin');
  }

  getEmployee(id: number): Observable<Employee> {
    return this.apiService.get<Employee>(`/usuarios/${id}`);
  }

  addEmployee(employee: Employee): Observable<Employee> {
    return this.apiService.post<Employee>('/usuarios/admin', employee);
  }

  updateEmployee(id: number, employee: Employee): Observable<Employee> {
    return this.apiService.put<Employee>(`/usuarios/admin/${id}`, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.apiService.delete<void>(`/usuarios/admin/${id}`);
  }
}