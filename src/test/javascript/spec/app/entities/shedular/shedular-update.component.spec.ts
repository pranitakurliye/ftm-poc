import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FtmpocTestModule } from '../../../test.module';
import { ShedularUpdateComponent } from 'app/entities/shedular/shedular-update.component';
import { ShedularService } from 'app/entities/shedular/shedular.service';
import { Shedular } from 'app/shared/model/shedular.model';

describe('Component Tests', () => {
  describe('Shedular Management Update Component', () => {
    let comp: ShedularUpdateComponent;
    let fixture: ComponentFixture<ShedularUpdateComponent>;
    let service: ShedularService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FtmpocTestModule],
        declarations: [ShedularUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ShedularUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShedularUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShedularService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Shedular(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Shedular();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
