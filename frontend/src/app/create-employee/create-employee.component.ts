import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmployeeService } from '../employee.service';
import { lastValueFrom } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrl: './create-employee.component.css'
})
export class CreateEmployeeComponent implements OnInit {
  
  // Get reference to the file input element
  @ViewChild('fileInput') fileInput!: ElementRef
  employeeForm!: FormGroup
  selectedFile: File | null = null

  private readonly empSvc = inject(EmployeeService)
  private readonly fb = inject(FormBuilder)
  private readonly router = inject(Router)

  ngOnInit(): void {
    this.createEmployeeForm()
  }

  createEmployeeForm() {
    this.employeeForm = this.fb.group({
      firstName: this.fb.control<string>('', [Validators.required, Validators.maxLength(64)]),
      lastName: this.fb.control<string>('', [Validators.required, Validators.maxLength(64)]),
      email: this.fb.control<string>('', [Validators.required, Validators.maxLength(125)]),
      file: this.fb.control('', [Validators.required])
    })
  }

  submit() {
    if (this.selectedFile) {
      const formData = new FormData()
      formData.set('firstName', this.employeeForm.get('firstName')?.value)
      formData.set('lastName', this.employeeForm.get('lastName')?.value)
      formData.set('email', this.employeeForm.get('email')?.value)
      formData.set('file', this.selectedFile)
      lastValueFrom(this.empSvc.createEmployee(formData))
        .then(result => {
          console.info(`>>>> Data successfully created: ${result}`)
          this.employeeForm.reset()
          // Navigate back to employee list
          this.router.navigate(['/employees']) 
    })
        .catch(error => console.error(`>>>>>>>>>> Data creation unsuccessful: ${error}`))
    } else {
      console.info('>>>>>>>>> No file selected')
      }
    }

  // Ensure that there is a file present when setting formdata
  onFileSelected() {
    const inputNode = this.fileInput.nativeElement
    // inputNode.files returns FileList - checks that files are not null and contains 1 or more files
    if (inputNode.files && inputNode.files.length > 0) {
      this.selectedFile = inputNode.files[0]
      console.info(`>>>>>>>> Selected file: ${this.selectedFile}`)
    } else {
      this.selectedFile = null
      console.info('>>>>>>> No file selected')
    }
  }

  // Additional method to allow users to reset file upload before submit
  resetFileInput() {
    this.fileInput.nativeElement.value = ''
    this.selectedFile = null
  }

}
