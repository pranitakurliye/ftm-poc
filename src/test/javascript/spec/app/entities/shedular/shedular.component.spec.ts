import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FtmpocTestModule } from '../../../test.module';
import { ShedularComponent } from 'app/entities/shedular/shedular.component';
import { ShedularService } from 'app/entities/shedular/shedular.service';
import { Shedular } from 'app/shared/model/shedular.model';

describe('Component Tests', () => {
  describe('Shedular Management Component', () => {
    let comp: ShedularComponent;
    let fixture: ComponentFixture<ShedularComponent>;
    let service: ShedularService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FtmpocTestModule],
        declarations: [ShedularComponent]
      })
        .overrideTemplate(ShedularComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShedularComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShedularService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Shedular(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.shedulars && comp.shedulars[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
