import { Moment } from 'moment';

export interface IShedular {
  id?: number;
  taskName?: string;
  taskShecduledAt?: Moment;
  status?: string;
}

export class Shedular implements IShedular {
  constructor(public id?: number, public taskName?: string, public taskShecduledAt?: Moment, public status?: string) {}
}
