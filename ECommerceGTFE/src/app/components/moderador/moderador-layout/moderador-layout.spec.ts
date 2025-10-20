import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeradorLayout } from './moderador-layout';

describe('ModeradorLayout', () => {
  let component: ModeradorLayout;
  let fixture: ComponentFixture<ModeradorLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModeradorLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModeradorLayout);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
