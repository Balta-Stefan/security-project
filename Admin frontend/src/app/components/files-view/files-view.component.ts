import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FileBasicDTO } from 'src/app/models/FileBasicDTO';
import { FileLogDTO } from 'src/app/models/FileLogDTO';
import { FileService } from 'src/app/services/file.service';

@Component({
  selector: 'app-files-view',
  templateUrl: './files-view.component.html',
  styleUrls: ['./files-view.component.css']
})
export class FilesViewComponent implements OnInit {

  files: FileBasicDTO[] = [];
  logs: FileLogDTO[] = [];

  constructor(private fileService: FileService) { }

  ngOnInit(): void {
    /*this.fileService.getRoot().subscribe({
      next: (value: FileBasicDTO[]) => {
        this.files = value;
      }, 
      error: (err: HttpErrorResponse) => {
        alert(`Could not obtain root directory, status: ${err.status}, message: ${err.statusText}`);
      }
    });*/
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
}
