import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IShedular, Shedular } from 'app/shared/model/shedular.model';
import { ShedularService } from './shedular.service';
import { ShedularComponent } from './shedular.component';
import { ShedularDetailComponent } from './shedular-detail.component';
import { ShedularUpdateComponent } from './shedular-update.component';

@Injectable({ providedIn: 'root' })
export class ShedularResolve implements Resolve<IShedular> {
  constructor(private service: ShedularService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShedular> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((shedular: HttpResponse<Shedular>) => {
          if (shedular.body) {
            return of(shedular.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Shedular());
  }
}

export const shedularRoute: Routes = [
  {
    path: '',
    component: ShedularComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ftmpocApp.shedular.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ShedularDetailComponent,
    resolve: {
      shedular: ShedularResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ftmpocApp.shedular.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ShedularUpdateComponent,
    resolve: {
      shedular: ShedularResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ftmpocApp.shedular.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ShedularUpdateComponent,
    resolve: {
      shedular: ShedularResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ftmpocApp.shedular.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
