import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticulosDisponibles } from './articulos-disponibles';

describe('ArticulosDisponibles', () => {
  let component: ArticulosDisponibles;
  let fixture: ComponentFixture<ArticulosDisponibles>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticulosDisponibles]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticulosDisponibles);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
