import { Component, OnInit, inject } from '@angular/core';
import { EmployeeService } from '../employee.service';
import { Employee } from '../employee';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrl: './employee-details.component.css'
})
export class EmployeeDetailsComponent implements OnInit {
  
  employee$!: Observable<Employee>
  id!: number
  
  private readonly empSvc = inject(EmployeeService)
  private readonly activatedRoute = inject(ActivatedRoute)
  
  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id']
    this.employee$ = this.empSvc.getEmployeeById(this.id)
  }

  
}
