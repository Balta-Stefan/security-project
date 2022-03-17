import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './components/main-page/main-page.component';
import { UsersViewComponent } from './components/users-view/users-view.component';

const routes: Routes = [
  {
    path: '', component: MainPageComponent, children: [
      {
        path: 'users', component: UsersViewComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
