import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Employee } from '../../../core/models/employee.model';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  errorMessage = '';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees() {
    this.adminService.getEmployees().subscribe({
      next: (data) => {
        this.employees = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar funcionários.';
        console.error('Erro:', error);
      }
    });
  }

  deleteEmployee(id: number) {
    if (confirm('Tem certeza que deseja excluir este funcionário?')) {
      this.adminService.deleteEmployee(id).subscribe({
        next: () => {
          this.loadEmployees(); // Recarregar a lista após a exclusão
        },
        error: (error) => {
          this.errorMessage = 'Erro ao excluir funcionário.';
          console.error('Erro:', error);
        }
      });
    }
  }
}