import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reporte1 } from './reporte1';

describe('Reporte1', () => {
  let component: Reporte1;
  let fixture: ComponentFixture<Reporte1>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reporte1]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reporte1);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
