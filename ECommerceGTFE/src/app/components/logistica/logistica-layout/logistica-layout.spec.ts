import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogisticaLayout } from './logistica-layout';

describe('LogisticaLayout', () => {
  let component: LogisticaLayout;
  let fixture: ComponentFixture<LogisticaLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogisticaLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogisticaLayout);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
