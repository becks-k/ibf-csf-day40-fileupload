import { Component, ElementRef, ViewChild, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { EmployeeService } from '../employee.service';
import { Employee } from '../employee';

@Component({
  selector: 'app-update-employee',
  templateUrl: './update-employee.component.html',
  styleUrl: './update-employee.component.css'
})
export class UpdateEmployeeComponent {
  // Get reference to the file input element
  @ViewChild('fileInput') fileInput!: ElementRef
  employeeForm!: FormGroup
  selectedFile: File | null = null
  id!: number
  employee!: Employee

  private readonly empSvc = inject(EmployeeService)
  private readonly fb = inject(FormBuilder)
  private readonly router = inject(Router)
  private readonly activatedRoute = inject(ActivatedRoute)

  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id']
    this.createEmployeeForm()
    this.empSvc.getEmployeeById(this.id).subscribe(emp => {
      // Patch value auto matches the fields sent in JsonObject to the form fields
      this.employeeForm.patchValue(emp)
    })
    
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
      lastValueFrom(this.empSvc.updateEmployee(this.id, formData))
        .then(result => {
          console.info(`>>>> Details successfully updated: ${result}`)
          this.employeeForm.reset()
          // Navigate back to employee list
          this.router.navigate(['/employees']) 
    })
        .catch(error => console.error(`>>>>>>>>>> Update unsuccessful: ${error}`))
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
