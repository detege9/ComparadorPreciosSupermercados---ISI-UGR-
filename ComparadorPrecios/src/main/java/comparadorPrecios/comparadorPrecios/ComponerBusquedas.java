package comparadorPrecios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;



//Clase encargada de integrar la información de las tres fuentes de datos.
//La información de dos de las fuentes de datos se consigue mediante web scraping. La restante se consigue
//accediendo a la API de Amazon.
public class ComponerBusquedas {
	String producto;
	ArrayList<Producto> listaProductos = new ArrayList<Producto>();
	
	public ComponerBusquedas(String str) {
		this.producto = str;
	}
	
	//Este método ordena los elementos de la clase Producto de menor a mayor en base al atributo precio.
	Comparator<Producto> compareByPrice = new Comparator<Producto>() {
		@Override
		public int compare(Producto p1, Producto p2) {
			float precio1 = p1.getPrecio();
			float precio2 = p2.getPrecio();
			
			return Float.compare(precio1, precio2);
		}
	};
	
	//Esta función crea un objeto de la clase Scraping y llama a la función buscar().
	//Esta función recopila la información de las dos fuentes de datos en una lista de productos que 
	//recogemos en nuestra listaProductos.
	private void busquedaScraping() throws IOException {
		Scraping s = new Scraping(producto);
		s.buscar();
		
		listaProductos = s.getListaProductos();
	}
	
	//Esta función crea un objeto de la clase JavaCodeSnippet y llama a la función buscarAmazon(). 
	//La función recoge la información de los productos y los va añadiendo a un vector de productos.
	//Ese vector de productos se añade a nuestra listaProductos.
	private void busquedaAmazon() throws IOException, ParserConfigurationException, SAXException {
		JavaCodeSnippet jcs = new JavaCodeSnippet(producto);
		jcs.buscarAmazon();
		
		listaProductos.addAll(jcs.getListaProductos());	
	}

	public ArrayList<Producto> getListaProductos(){
    	return this.listaProductos;
    }
	
	//Es la función que llama a las dos funciones principales de esta clase. 
	//Por último, ordena la lista de productos comparándolos por el atributo precio.
	public void busqueda() throws IOException, ParserConfigurationException, SAXException {
		busquedaScraping();
		busquedaAmazon();
		
		Collections.sort(listaProductos, compareByPrice);
	}
	
	
}
