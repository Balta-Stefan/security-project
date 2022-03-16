import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './components/main-page/main-page.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { UsersViewComponent } from './components/users-view/users-view.component';

const routes: Routes = [
  {
    path: '', component: MainPageComponent, children: [
      {
        path: 'users', component: UsersViewComponent
      },
      {
        path: 'notifications', component: NotificationsComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
