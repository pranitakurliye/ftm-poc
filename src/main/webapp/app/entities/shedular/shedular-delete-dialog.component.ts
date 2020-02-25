import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShedular } from 'app/shared/model/shedular.model';
import { ShedularService } from './shedular.service';

@Component({
  templateUrl: './shedular-delete-dialog.component.html'
})
export class ShedularDeleteDialogComponent {
  shedular?: IShedular;

  constructor(protected shedularService: ShedularService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shedularService.delete(id).subscribe(() => {
      this.eventManager.broadcast('shedularListModification');
      this.activeModal.close();
    });
  }
}
