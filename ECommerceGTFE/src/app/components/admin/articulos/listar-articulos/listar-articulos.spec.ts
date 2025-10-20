import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarArticulos } from './listar-articulos';

describe('ListarArticulos', () => {
  let component: ListarArticulos;
  let fixture: ComponentFixture<ListarArticulos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarArticulos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListarArticulos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
