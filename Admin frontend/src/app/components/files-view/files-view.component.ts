import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DirectoryBasicDTO } from 'src/app/models/DirectoryBasicDTO';
import { FileBasicDTO } from 'src/app/models/FileBasicDTO';
import { FileLogDTO } from 'src/app/models/FileLogDTO';
import { DirectoryDTO } from 'src/app/models/DirectoryDTO';
import { FileService } from 'src/app/services/file.service';
import { LogsModalComponent } from '../logs-modal/logs-modal.component';

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
  newFile!: File;

  constructor(private fileService: FileService, private dialog: MatDialog) { }

  ngOnInit(): void {
    /*this.fileService.getRoot().subscribe({
      next: (value: DirectoryDTO) => {
        this.workingDir = value.directory;
        this.files = value.children;
      }, 
      error: (err: HttpErrorResponse) => {
        alert(`Could not obtain root directory, status: ${err.status}, message: ${err.statusText}`);
      }
    });*/
    const oneFile: FileBasicDTO = {
      fileId: 1,
      isDirectory: true,
      name: "First dir"
    }
    this.files.push(oneFile);
  }

  getFileLogs(selectedFile: FileBasicDTO): void{
    this.fileService.getFileLogs(selectedFile.fileId).subscribe({
      next: (receivedLogs: FileLogDTO[]) => {
        this.logs = receivedLogs;
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occured: " + err.statusText);
      }
    });
  }

  breadcrumbSelected(dir: DirectoryBasicDTO): void{
    
  }

  deleteFile(file: FileBasicDTO): void{
    this.fileService.deleteFile(file.fileId).subscribe({
      complete: () => {
        this.files = this.files.filter(f => f.fileId != file.fileId);
      },
      error: (err: HttpErrorResponse) => {
        alert("Error: " + err.statusText);
      }
    });
  }

  openLogsModal(file: FileBasicDTO): void{
    sessionStorage.setItem("chosenFileForLogs", file.fileId.toString());
    const dialogRef = this.dialog.open(LogsModalComponent, {
      height: '100%',
      panelClass: 'responsive-material-modal'
    });
  }

  moveFile(file: FileBasicDTO): void{

  }

  openDir(file: FileBasicDTO): void{
    this.fileService.listDir(file.fileId).subscribe({
      next: (values: DirectoryDTO) => {
        this.breadcrumbs.push(this.workingDir);
        this.workingDir = values.directory;

        this.files = values.children;
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occurred: " + err.statusText);
      }
    });
  }

  createDir(): void{
    
  }

  uploadFile(): void{
    this.fileService.uploadFile(this.workingDir.fileID, this.newFile).subscribe({
      next: (newFile: FileBasicDTO) => {
        this.files.push(newFile);
        alert("File successfully uploaded.");
      },
      error: (err: HttpErrorResponse) => {
        alert("An error has occurred: " + err.statusText);
      }
    });
  }

  newFileChosen(event: Event): void{
    const target = event.target as HTMLInputElement;
    this.newFile = (target.files as FileList)[0];
  }
}
