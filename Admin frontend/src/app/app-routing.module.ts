import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FilesViewComponent } from './components/files-view/files-view.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { UsersViewComponent } from './components/users-view/users-view.component';

const routes: Routes = [
  {
    path: '', component: MainPageComponent, children: [
      {
        path: 'files', component: FilesViewComponent
      },
      {
        path: 'users', component: UsersViewComponent
      }
    ]
  }
];


/*{path: '', canActivate: [RouteGuardService], canActivateChild: [RouteGuardService],component: LibraryDashboardComponent, children: [
        {path: 'knjige', component: BooksPanelComponent},
        {path: 'seminarski_radovi', component: SeminaryGraduateWorksDashboardComponent},
        {path: 'casopisi', component: MagazinesDashboardComponent},
        {path: 'nalozi/:id', component: AccountOverviewComponent},
        {path: 'nalozi', component: AccountsPanelComponent},
        {path: 'pozajmljivanja', component: LoansPanelComponent},
        {path: 'rezervacije', component: ReservationsDashboardComponent},
        {path: 'statistike', component: StatsPanelComponent}
      ]},
      {path: 'prijava', component: LoginPanelComponent}
*/

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
