import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DirectoryBasicDTO } from 'src/app/models/DirectoryBasicDTO';
import { FileBasicDTO } from 'src/app/models/FileBasicDTO';
import { FileLogDTO } from 'src/app/models/FileLogDTO';
import { DirectoryDTO } from 'src/app/models/DirectoryDTO';
import { FileService } from 'src/app/services/file.service';
import { LogsModalComponent } from '../logs-modal/logs-modal.component';
import { Role } from 'src/app/models/Role';
import { ApplicationService } from 'src/app/services/application.service';
import { MoveFileModalComponent } from '../move-file-modal/move-file-modal.component';

@Component({
  selector: 'app-files-view',
  templateUrl: './files-view.component.html',
  styleUrls: ['./files-view.component.css']
})
export class FilesViewComponent implements OnInit {
  files: FileBasicDTO[] = [];
  logs: FileLogDTO[] = [];

  breadcrumbs: DirectoryBasicDTO[] = [];
  workingDir!: DirectoryBasicDTO;

  newDirName!: string;
  newFile: File | null = null;

  role!: Role;

  @ViewChild('fileUploadInput') fileUploadInput!: ElementRef;

  constructor(private fileService: FileService, private dialog: MatDialog, private appService: ApplicationService) { }

  private modifyBreadcrumbNames(): void{
    if(this.workingDir){
      this.workingDir.name = '(' + this.workingDir.fileId + ')' + this.workingDir.name;
    }
    
    if(this.breadcrumbs){
      this.breadcrumbs.forEach(b => b.name = '(' + b.fileId + ')' + b.name);
    }
  }

  ngOnInit(): void {
    this.role = this.appService.getRole();

    this.fileService.getRoot().subscribe({
      next: (value: DirectoryDTO) => {
        this.workingDir = value.directory!;
        this.files = value.children;

        this.modifyBreadcrumbNames();
      }, 
      error: (err: HttpErrorResponse) => {
        alert(`Could not obtain root directory, status: ${err.status}, message: ${err.status}`);
      }
    });
    /*const oneFile: FileBasicDTO = {
      fileId: 1,
      isDirectory: true,
      name: "First dir"
    }
    this.files.push(oneFile);*/
  }

  getFileLogs(selectedFile: FileBasicDTO): void{
    this.fileService.getFileLogs(selectedFile.fileId).subscribe({
      next: (receivedLogs: FileLogDTO[]) => {
        this.logs = receivedLogs;
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occured: " + err.status);
      }
    });
  }

  breadcrumbSelected(dir: DirectoryBasicDTO): void{
    this.fileService.listDir(dir.fileId).subscribe({
      next: (value: DirectoryDTO) => {
        this.workingDir = value.directory!;
        this.breadcrumbs = value.breadCrumbs!;
        this.files = value.children;

        this.modifyBreadcrumbNames();
      },
      error: (err: HttpErrorResponse) => {
        alert("Could not retrieve directory: " + err.status);
      }
    });
  }

  deleteFile(file: FileBasicDTO): void{
    this.fileService.deleteFile(file.fileId).subscribe({
      complete: () => {
        this.files = this.files.filter(f => f.fileId != file.fileId);
      },
      error: (err: HttpErrorResponse) => {
        alert("Error: " + err.status);
      }
    });
  }

  openLogsModal(file: FileBasicDTO): void{
    sessionStorage.setItem("chosenFileForLogs", file.fileId.toString());
    const dialogRef = this.dialog.open(LogsModalComponent, {
      height: '100%',
      width: '60%',
      panelClass: 'responsive-material-modal'
    });
  }

  moveFile(file: FileBasicDTO): void{
    const dialogRef = this.dialog.open(MoveFileModalComponent, {
      panelClass: 'responsive-material-modal'
    });
    dialogRef.componentInstance.fileToMoveID = file.fileId;
    dialogRef.afterClosed().subscribe({
      next: (value: any) => {
        if(value){
          this.files = this.files.filter(f => f.fileId != file.fileId);
        }
      }
    });
  }

  openDir(file: FileBasicDTO): void{
    this.fileService.listDir(file.fileId).subscribe({
      next: (values: DirectoryDTO) => {
        this.breadcrumbs = values.breadCrumbs!;
        this.workingDir = values.directory!;
        this.files = values.children;

        this.modifyBreadcrumbNames();
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occurred: " + err.status);
      }
    });
  }

  createDir(): void{
    this.fileService.createDirectory(this.workingDir.fileId, this.newDirName).subscribe({
      next: (newDir: FileBasicDTO) => {
        this.files.push(newDir);
        alert("Diretory successfully created.");
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occurred: " + err.status);
      }
    });
  }

  uploadFile(): void{
    this.fileService.uploadFile(this.workingDir.fileId, this.newFile!).subscribe({
      next: (newFile: FileBasicDTO) => {
        this.files.push(newFile);
        this.newFile = null;
        this.fileUploadInput.nativeElement.value = '';
        alert("File successfully uploaded.");
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occurred: " + err.status);
      }
    });
  }

  newFileChosen(event: Event): void{
    const target = event.target as HTMLInputElement;
    this.newFile = (target.files as FileList)[0];
  }
}
