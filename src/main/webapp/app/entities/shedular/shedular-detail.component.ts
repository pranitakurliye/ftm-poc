import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShedular } from 'app/shared/model/shedular.model';

@Component({
  selector: 'jhi-shedular-detail',
  templateUrl: './shedular-detail.component.html'
})
export class ShedularDetailComponent implements OnInit {
  shedular: IShedular | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shedular }) => (this.shedular = shedular));
  }

  previousState(): void {
    window.history.back();
  }
}
