import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicarArticulo } from './publicar-articulo';

describe('PublicarArticulo', () => {
  let component: PublicarArticulo;
  let fixture: ComponentFixture<PublicarArticulo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicarArticulo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicarArticulo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
