import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reporte5 } from './reporte5';

describe('Reporte5', () => {
  let component: Reporte5;
  let fixture: ComponentFixture<Reporte5>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reporte5]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reporte5);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
