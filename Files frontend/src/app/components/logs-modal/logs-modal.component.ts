import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FileLogDTO } from 'src/app/models/FileLogDTO';
import { FileService } from 'src/app/services/file.service';

@Component({
  selector: 'app-logs-modal',
  templateUrl: './logs-modal.component.html',
  styleUrls: ['./logs-modal.component.css']
})
export class LogsModalComponent implements OnInit {
  logs: FileLogDTO[] = [];

  constructor(private fileService: FileService) { }

  ngOnInit(): void {
    const fileId: number = +sessionStorage.getItem('chosenFileForLogs')!;

    this.fileService.getFileLogs(fileId).subscribe({
      next: (value: FileLogDTO[]) => {
        this.logs = value;
      },
      error: (err: HttpErrorResponse) => {
        alert("Error: " + err.status);
      }
    });
  }

}
