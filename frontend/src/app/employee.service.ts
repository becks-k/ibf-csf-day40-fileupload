import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Employee } from './employee';
import { Observable, lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  baseUrl: string = 'https://incredible-contentment-production.up.railway.app'

  private readonly http = inject(HttpClient)

  // Plug Promise with async into html
  getAllEmployees(): Promise<Employee[]> {
    return lastValueFrom(this.http.get<Employee[]>(`${this.baseUrl}/employees`))
  }

  // Plug Observable with async into html
  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/employee/${id}`)
  }

  // Save formdata which contains multipartfile and form fields
  createEmployee(formData: FormData): Observable<Object> {
    return this.http.post(`${this.baseUrl}/save`, formData)
  }

  updateEmployee(id: number, formData: FormData): Observable<Object> {
    return this.http.post(`${this.baseUrl}/update/${id}`, formData)
  }

  deleteEmployee(id: number): Observable<Object> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`)
  }

}
