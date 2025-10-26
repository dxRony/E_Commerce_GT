import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reporte4 } from './reporte4';

describe('Reporte4', () => {
  let component: Reporte4;
  let fixture: ComponentFixture<Reporte4>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reporte4]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reporte4);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
