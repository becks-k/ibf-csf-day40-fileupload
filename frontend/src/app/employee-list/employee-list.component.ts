import { Component, OnInit, inject } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';
import { Observable, Subject, lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.css'
})
export class EmployeeListComponent implements OnInit {
  
  employees$!: Promise<Employee[]>
  deleteSub$!: Observable<Object>

  private readonly empSvc = inject(EmployeeService)

  
  ngOnInit(): void {
    this.employees$ = this.empSvc.getAllEmployees()
  }

  delete(id: number) {
    this.employees$ = lastValueFrom(this.empSvc.deleteEmployee(id))
      .then(result => {
        console.info('>>>>>>>> DELETED')
        return this.empSvc.getAllEmployees()
      })
  }
}
