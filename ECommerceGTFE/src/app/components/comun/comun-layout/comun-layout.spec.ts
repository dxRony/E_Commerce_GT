import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComunLayout } from './comun-layout';

describe('ComunLayout', () => {
  let component: ComunLayout;
  let fixture: ComponentFixture<ComunLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComunLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComunLayout);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
