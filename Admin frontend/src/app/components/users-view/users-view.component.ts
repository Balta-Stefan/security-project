import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FileBasicDTO } from 'src/app/models/FileBasicDTO';
import { Role } from 'src/app/models/Role';
import { UserInfoDTO } from 'src/app/models/UserInfoDTO';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-users-view',
  templateUrl: './users-view.component.html',
  styleUrls: ['./users-view.component.css']
})
export class UsersViewComponent implements OnInit {
  userFormData: FormGroup;
  filterUsersFormData: FormGroup;

  roles = Role;

  submitUserInfoIsError: boolean = false;
  submitUserInfoMessage: string = "";

  filteredUsers: UserInfoDTO[] = [];

  selectedUser!: UserInfoDTO;

  constructor(private fb: FormBuilder,
              private userService: UserService) { 

    this.filterUsersFormData = fb.group({
      username: null,
      role: null
    });

    this.userFormData = fb.group({
      role: null,
      active: null,
      canCreate: null,
      canRead: null,
      canUpdate: null,
      canDelete: null,
      accessFromIp: null,
      accessFromDomain: null,
      rootDirID: null
    });
  }

  ngOnInit(): void {
  }

  userSelected(user: UserInfoDTO): void{
    this.selectedUser = user;
    Object.assign(this.selectedUser, user);

    this.userFormData = this.fb.group(user);
  }

  getUsers(): void{
    this.selectedUser = null!;
    this.submitUserInfoMessage = "";

    this.userService.filterUsers(this.filterUsersFormData.value['username'], this.filterUsersFormData.value['role']).subscribe({
      next: (value: UserInfoDTO[]) => {
        this.filteredUsers = value;
      },
      error: (err: HttpErrorResponse) => {
        this.submitUserInfoIsError = true;
        this.submitUserInfoMessage = err.status.toString();
      }
    });
  }

  changeUserInfo(): void{
    let userInfo = this.userFormData.value;

    this.submitUserInfoMessage = "";

    this.userService.changeUser(userInfo).subscribe({
      next: (value: UserInfoDTO) => {
        this.userSelected(value);

        this.submitUserInfoIsError = false;
        this.submitUserInfoMessage = "User modified.";
      },
      error: (err: HttpErrorResponse) => {
        this.submitUserInfoIsError = true;
        this.submitUserInfoMessage = err.status.toString();
      }
    });
  }
}
