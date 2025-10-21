import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarArticulo } from './editar-articulo';

describe('EditarArticulo', () => {
  let component: EditarArticulo;
  let fixture: ComponentFixture<EditarArticulo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarArticulo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarArticulo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
