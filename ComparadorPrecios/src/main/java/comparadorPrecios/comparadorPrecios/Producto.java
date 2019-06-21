package comparadorPrecios;


//Clase encargado de transformar adecuadamente y almacenar la información de los productos.
//De cada producto se almacena el precio, el nombre del producto, la URL al producto en la fuente de datos correspondiente,
//la URL de la imagen y una variable booleana para saber el formato en el que aparece el precio.
public class Producto {
	float precio;
	String producto;
	String urlProducto;
	String urlImagen;
	boolean precioKilo;
	
	//Esta función se encarga de transformar el precio a un formato adecuado para poder procesarlo posteriormente
	//a la hora de ordenar los precios de forma ascendente.
	private float convertirAPrecio(String strPrecio) {
		String tmpPrecio;       
        
		//Si el precio tiene presente '/kg', el formato del precio es diferente al resto y lo almacenamos
		//en la variable booleana.
		//PD: â,¬ es el signo del €.
        if(strPrecio.contains("/kg")){
            this.precioKilo = true;
            
            //Si aparece €/kg, se cambia por nada.
            tmpPrecio = strPrecio.replace( "â‚¬/kg", "" );
        }
        
        //Si no aparece el /kg, el formato del precio es normal.
        else{
            this.precioKilo = false;
            tmpPrecio = strPrecio.replace( "â‚¬", "" );
        }
        
        //Después del if-else anterior, el formato del precio será siempre el siguiente 2,22 y nosotros queremos
        //que la coma sea un punto para poder procesarlo de forma correcta.
        tmpPrecio = tmpPrecio.replace(",",".");
        
        //Se convierte el string 'tmpPrecio' a float.
        return Float.parseFloat(tmpPrecio); 
	}

    Producto(String pre, String pro, String urlPro, String urlImg){
    	this.precio = convertirAPrecio(pre);
		this.producto = pro;
		this.urlProducto = urlPro;
		this.urlImagen = urlImg;
    }
    
    public float getPrecio() {
    	return this.precio;
    }
    
    public String getProducto() {
    	return this.producto;
    }
    
    public String getUrlProducto() {
    	return this.urlProducto;
    }
    
    public String getUrlImagen() {
    	return this.urlImagen;
    }
    
    public String esPrecioKilo() {
    	if (precioKilo)
    		return "â‚¬/kg";
    	return "â‚¬";
    }
}
