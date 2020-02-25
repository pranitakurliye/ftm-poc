import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FtmpocTestModule } from '../../../test.module';
import { ShedularDetailComponent } from 'app/entities/shedular/shedular-detail.component';
import { Shedular } from 'app/shared/model/shedular.model';

describe('Component Tests', () => {
  describe('Shedular Management Detail Component', () => {
    let comp: ShedularDetailComponent;
    let fixture: ComponentFixture<ShedularDetailComponent>;
    const route = ({ data: of({ shedular: new Shedular(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FtmpocTestModule],
        declarations: [ShedularDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ShedularDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShedularDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load shedular on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.shedular).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
