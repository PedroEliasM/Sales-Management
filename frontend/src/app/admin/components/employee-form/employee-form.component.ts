import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Employee } from '../../../core/models/employee.model';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss']
})
export class EmployeeFormComponent implements OnInit {
  employeeForm: FormGroup;
  isEditMode = false;
  employeeId: number | null = null;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.employeeForm = this.fb.group({
      nome: ['', Validators.required],
      sobrenome: ['', Validators.required],
      idade: [null, [Validators.required, Validators.min(18)]],
      cidade: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefone: [''],
      senha: ['', this.isEditMode ? [] : Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.employeeId = params['id'];
      if (this.employeeId) {
        this.isEditMode = true;
        this.loadEmployeeDetails(this.employeeId);
        this.employeeForm.get('senha')?.clearValidators();
        this.employeeForm.get('senha')?.updateValueAndValidity();
      }
    });
  }

  loadEmployeeDetails(id: number) {
    this.adminService.getEmployee(id).subscribe({
      next: (employee) => {
        this.employeeForm.patchValue(employee);
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar detalhes do funcionário.';
        console.error('Erro:', error);
      }
    });
  }

  onSubmit() {
    if (this.employeeForm.valid) {
      const employeeData: Employee = this.employeeForm.value;
      if (this.isEditMode && this.employeeId) {
        this.adminService.updateEmployee(this.employeeId, employeeData).subscribe({
          next: () => {
            this.router.navigate(['/admin/employees']);
          },
          error: (error) => {
            this.errorMessage = 'Erro ao atualizar funcionário.';
            console.error('Erro:', error);
          }
        });
      } else {
        this.adminService.addEmployee(employeeData).subscribe({
          next: () => {
            this.router.navigate(['/admin/employees']);
          },
          error: (error) => {
            this.errorMessage = 'Erro ao adicionar funcionário.';
            console.error('Erro:', error);
          }
        });
      }
    }
  }
}