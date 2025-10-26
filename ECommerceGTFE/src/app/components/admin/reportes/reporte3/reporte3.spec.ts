import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reporte3 } from './reporte3';

describe('Reporte3', () => {
  let component: Reporte3;
  let fixture: ComponentFixture<Reporte3>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reporte3]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reporte3);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
