import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisArticulos } from './mis-articulos';

describe('MisArticulos', () => {
  let component: MisArticulos;
  let fixture: ComponentFixture<MisArticulos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisArticulos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisArticulos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
