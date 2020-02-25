import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IShedular } from 'app/shared/model/shedular.model';
import { ShedularService } from './shedular.service';
import { ShedularDeleteDialogComponent } from './shedular-delete-dialog.component';

@Component({
  selector: 'jhi-shedular',
  templateUrl: './shedular.component.html'
})
export class ShedularComponent implements OnInit, OnDestroy {
  shedulars?: IShedular[];
  eventSubscriber?: Subscription;

  constructor(protected shedularService: ShedularService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.shedularService.query().subscribe((res: HttpResponse<IShedular[]>) => (this.shedulars = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInShedulars();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IShedular): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInShedulars(): void {
    this.eventSubscriber = this.eventManager.subscribe('shedularListModification', () => this.loadAll());
  }

  delete(shedular: IShedular): void {
    const modalRef = this.modalService.open(ShedularDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.shedular = shedular;
  }
}
