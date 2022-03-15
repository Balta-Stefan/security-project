import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FileService } from 'src/app/services/file.service';

@Component({
  selector: 'app-move-file-modal',
  templateUrl: './move-file-modal.component.html',
  styleUrls: ['./move-file-modal.component.css']
})
export class MoveFileModalComponent implements OnInit {

  fileToMoveID!: number;
  destination!: number;

  constructor(private fileService: FileService, private dialogRef: MatDialogRef<MoveFileModalComponent>) { }

  ngOnInit(): void {
  }

  moveFile(): void{
    this.fileService.moveFile(this.fileToMoveID, this.destination).subscribe({
      complete: () => {
        alert("File successfully moved.");
        this.dialogRef.close(true);
      },
      error: (err: HttpErrorResponse) => {
        alert("Error: " + err.status);
      }
    });
  }
}
