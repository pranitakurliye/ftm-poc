import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IShedular } from 'app/shared/model/shedular.model';

type EntityResponseType = HttpResponse<IShedular>;
type EntityArrayResponseType = HttpResponse<IShedular[]>;

@Injectable({ providedIn: 'root' })
export class ShedularService {
  public resourceUrl = SERVER_API_URL + 'api/shedulars';

  constructor(protected http: HttpClient) {}

  create(shedular: IShedular): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shedular);
    return this.http
      .post<IShedular>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(shedular: IShedular): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shedular);
    return this.http
      .put<IShedular>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IShedular>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IShedular[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(shedular: IShedular): IShedular {
    const copy: IShedular = Object.assign({}, shedular, {
      taskShecduledAt: shedular.taskShecduledAt && shedular.taskShecduledAt.isValid() ? shedular.taskShecduledAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.taskShecduledAt = res.body.taskShecduledAt ? moment(res.body.taskShecduledAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((shedular: IShedular) => {
        shedular.taskShecduledAt = shedular.taskShecduledAt ? moment(shedular.taskShecduledAt) : undefined;
      });
    }
    return res;
  }
}
