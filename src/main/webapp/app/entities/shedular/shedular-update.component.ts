import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IShedular, Shedular } from 'app/shared/model/shedular.model';
import { ShedularService } from './shedular.service';

@Component({
  selector: 'jhi-shedular-update',
  templateUrl: './shedular-update.component.html'
})
export class ShedularUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    taskName: [],
    taskShecduledAt: [],
    status: []
  });

  constructor(protected shedularService: ShedularService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shedular }) => {
      if (!shedular.id) {
        const today = moment().startOf('day');
        shedular.taskShecduledAt = today;
      }

      this.updateForm(shedular);
    });
  }

  updateForm(shedular: IShedular): void {
    this.editForm.patchValue({
      id: shedular.id,
      taskName: shedular.taskName,
      taskShecduledAt: shedular.taskShecduledAt ? shedular.taskShecduledAt.format(DATE_TIME_FORMAT) : null,
      status: shedular.status
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shedular = this.createFromForm();
    if (shedular.id !== undefined) {
      this.subscribeToSaveResponse(this.shedularService.update(shedular));
    } else {
      this.subscribeToSaveResponse(this.shedularService.create(shedular));
    }
  }

  private createFromForm(): IShedular {
    return {
      ...new Shedular(),
      id: this.editForm.get(['id'])!.value,
      taskName: this.editForm.get(['taskName'])!.value,
      taskShecduledAt: this.editForm.get(['taskShecduledAt'])!.value
        ? moment(this.editForm.get(['taskShecduledAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShedular>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
