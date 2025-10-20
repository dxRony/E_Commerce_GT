import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PedidosEnCurso } from './pedidos-en-curso';

describe('PedidosEnCurso', () => {
  let component: PedidosEnCurso;
  let fixture: ComponentFixture<PedidosEnCurso>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidosEnCurso]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PedidosEnCurso);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
