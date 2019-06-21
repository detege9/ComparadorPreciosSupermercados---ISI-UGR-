package comparadorPrecios;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.Connection.Response;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

//Esta clase se encarga de realizar el WebScraping de las dos fuentes de datos, Hipercor y Carrefour.
public class Scraping {
	String producto;
	
	ArrayList<Producto> listaProductos = new ArrayList<Producto>();
	
	//En el constructor, para poder formar las URL's de forma correcta, los espacios serán convertidos en +.
	//Pechuga de pollo --> Pechuga+de+pollo.
    public Scraping(String str) {
    	this.producto = str.replaceAll("\\s+","+");
    }
    
    //Esta función se encarga de sacar la información de Hipercor.
    private void buscarHipercor() throws IOException {
    	
    	//Se guarda en una variable la parte de URL de Hipercor que nos interesa para poder juntarla con el producto
    	//y formar la URL de forma correcta.
		String url1 = "https://www.hipercor.es/supermercado/buscar/?term=";
		String urlPage = url1+this.producto;
				  
		System.out.println("Comprobando entradas de: "+urlPage);
		 
		// Compruebo si me da un 200 al hacer la peticiÃ³n
		if (getStatusConnectionCode(urlPage) == 200) {

			// Obtengo el HTML de la web en un objeto Document2
			Document document = getHtmlDocument(urlPage);
		  
			// Busco todas las historias de Hipercor que estan dentro de:
			Elements entradas = document.select("div.grid-item.product_tile._hipercor.dataholder.js-product._mobilebuyable.js-buyable-layer");
		  
			// Paseo cada una de las entradas
			for (Element elem : entradas) {
				String titulo = "";
				String precio = "";
				String link = "";
				String urlImagen = "";
		
				//Examinando el código HTML, se observa que la información que nos interesa
				//está dentro de etiquetas que tienen como clase estas descripciones.
				titulo = elem.getElementsByClass("product_tile-description").text();
				precio = elem.getElementsByClass("prices-price _current").text();
				
				//En Hipercor, para el precio utilizan varias clases, dependiendo si 
				//el precio está expresado en €/kg o si el precio está en oferta.
				if (precio.isEmpty()) {
                    precio = elem.getElementsByClass("prices-price _offer").text();
                    
                    if (precio.isEmpty()) {              
                        precio = elem.getElementsByClass("prices-price _offer _no_pum").text();
                    
	                    if (precio.isEmpty())
	                        precio = elem.getElementsByClass("prices-price _current _no_pum").text();
                    }
				}			
				
				//El link del producto se extrae accediendo a la etiqueta 'a' y al atributo href. 
				//Se ha tenido que añadir 'https://www.hipercor.es' a la URL ya que
				//solo devolvía la parte restante de la URL.
				link = "https://www.hipercor.es" + elem.getElementsByTag("a").attr("href");
				
				//Con la imagen pasa un caso parecido al anterior, devolvía la URL sin 
				//el 'https:'.
				urlImagen = "https:"+elem.getElementsByTag("img").attr("src");
				

				Producto p = new Producto(precio, titulo, link, urlImagen);
				listaProductos.add(p);	

			}               
		}else{
			System.out.println("El Status Code no es OK es: "+getStatusConnectionCode(urlPage));
		}     
	}
	
  //Esta función se encarga de sacar la información de Hipercor.
	private void buscarCarrefour() throws IOException {    
		
    	//Se guardan en dos variables las partes de URL necesarias para poder consultar los productos.
		//Y se relacionan estas dos partes de URL añadiéndole el producto entre medias.
    	String url1 = "https://www.carrefour.es/global/?Dy=1&Nty=1&Ntx=mode+matchallany&Ntt=";
    	String url2 = "&search=Buscar";    	
    	
        String urlPage = url1+this.producto+url2;
        System.out.println("Comprobando entradas de: "+urlPage);
		
        // Compruebo si me da un 200 al hacer la peticiÃ³n
        if (getStatusConnectionCode(urlPage) == 200) {
			
            // Obtengo el HTML de la web en un objeto Document2
            Document document = getHtmlDocument(urlPage);
			
            // Busco todas los productos de Carrefour que estan dentro de:
            Elements entradas = document.select("div.col-sm-6.col-md-4.col-xs-12.col-s-6");
			
            // Paseo cada una de las entradas
            for (Element elem : entradas) {
            	
            	//Examinando el código HTML de Carrefour, se comprueba que la información
            	//está contenida en etiquetas que tienen como clase las clases siguientes.
            	//A diferencia de con Hipercor, el precio no tiene ninguna distinción, 
            	//el enlace del producto está entero y la imagen también.
                String titulo = elem.getElementsByClass("titular-producto").text();
                String precio = elem.getElementsByClass("precio-nuevo").text();
                String link = elem.getElementsByClass("track-click enlace-producto").attr("href");
                String urlImagen = elem.getElementsByClass("img-producto").attr("src");
                
                Producto p = new Producto(precio, titulo, link, urlImagen);
				listaProductos.add(p);                			
            }
	
        }else{
            System.out.println("El Status Code no es OK es: "+getStatusConnectionCode(urlPage));
        }	
    }
    
    public ArrayList<Producto> getListaProductos(){
    	return this.listaProductos;
    }
	
	public void buscar() throws IOException {
		buscarCarrefour();
		buscarHipercor();
	}
	
    /**
     * Con esta mÃ©todo compruebo el Status code de la respuesta que recibo al hacer la peticiÃ³n
     * EJM:
     * 		200 OK			300 Multiple Choices
     * 		301 Moved Permanently	305 Use Proxy
     * 		400 Bad Request		403 Forbidden
     * 		404 Not Found		500 Internal Server Error
     * 		502 Bad Gateway		503 Service Unavailable
     * @param url
     * @return Status Code
     */
    public static int getStatusConnectionCode(String url) throws IOException {
		
        Response response = null;
		
        response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        return response.statusCode();
    }
	
	
    /**
     * Con este mÃ©todo devuelvo un objeto de la clase Document con el contenido del
     * HTML de la web que me permitirÃ¡ parsearlo con los mÃ©todos de la librelia JSoup
     * @param url
     * @return Documento con el HTML
     */
    public static Document getHtmlDocument(String url) throws IOException {

        Document doc = null;

        doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();

        return doc;

    }

}
