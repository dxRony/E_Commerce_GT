import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reporte2 } from './reporte2';

describe('Reporte2', () => {
  let component: Reporte2;
  let fixture: ComponentFixture<Reporte2>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reporte2]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reporte2);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
