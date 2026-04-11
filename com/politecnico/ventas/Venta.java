package com.politecnico.ventas;

public class Venta {
	
    Vendedor vendedor;
    
    Producto producto;
    
    int cantidad;

    public Venta(Vendedor vendedor, Producto producto, int cantidad) {
    	
        this.vendedor = vendedor;
        
        this.producto = producto;
        
        this.cantidad = cantidad;
        
    }
    
}
