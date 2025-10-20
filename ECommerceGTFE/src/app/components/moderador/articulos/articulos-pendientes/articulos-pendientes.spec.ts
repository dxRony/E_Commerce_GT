import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticulosPendientes } from './articulos-pendientes';

describe('ArticulosPendientes', () => {
  let component: ArticulosPendientes;
  let fixture: ComponentFixture<ArticulosPendientes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticulosPendientes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticulosPendientes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
