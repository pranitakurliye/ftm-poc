import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'shedular',
        loadChildren: () => import('./shedular/shedular.module').then(m => m.FtmpocShedularModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class FtmpocEntityModule {}
