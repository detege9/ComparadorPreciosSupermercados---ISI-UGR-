<%@page import="java.util.ArrayList"%>
<%@page import="comparadorPrecios.*"%>
<%@page import="org.w3c.dom.Document"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="es">
<head>
<meta http-equiv="content-type"
	content="application/xhtml+xml; charset=UTF-8" />
<title>Comparador de precios</title>

<link rel="icon" type="image/png" href="imagenes/icono.png"
	sizes="16x16">
<link rel="stylesheet" type="text/css" href="css/index.css">
</head>

<body>
	<header>
		<a href="https://comparador-de-precios-235614.appspot.com"> <img
			src="img/logo.png" class="header-logo" alt="Logo">
		</a>

		<h1 class="titulo-web">Comparador de precios de supermercados</h1>
	</header>

	<form action="busqueda.jsp" method="get" class="search-form">
		<fieldset>
			<legend>Búsqueda</legend>

			<img src="img/lupa.png" class="img-lupa" alt="lupa">
			<input	type="search" id="busqueda" name="producto" class="search-box" autofocus>
			<input type="submit" class="search-box" value="Buscar">

		</fieldset>
	</form>

	<!--section class="lista-productos">
	<h2>Productos</h2-->
	<h2>Productos</h2>
	<ul class="lista-productos">
		<%
		//Para recoger el valor de un formulario en JSP, se utiliza la función request.getParameter.
		//En la variable producto se va a guardar lo introducido por el usuario.
		String producto = request.getParameter("producto");
		
		//Se crea un objeto de la clase ComponerBusquedas, que es la encargada de confeccionar la salida
		//añadiendo los productos obtenidos de las diferentes fuentes de datos.
		ComponerBusquedas cb = new ComponerBusquedas(producto);
		cb.busqueda();
		
		//listaProductos contiene todos los productos (título, imagen, imagen, precio y URL).
		ArrayList<Producto> listaProductos = cb.getListaProductos();
		
		//Se realiza un bucle para recorrer todos los productos de la lista e ir mostrándolo en la página web.
		for(int i=0 ; i<listaProductos.size() ; i++){
			Producto p = listaProductos.get(i);
			
			//Se consulta la URL del producto para saber de dónde viene. Esto se hace para poder
			//qué imagen poner al lado de cada producto (dependiendo de la tienda origen).
			String pathLogo;
			if(p.getUrlProducto().contains("hipercor")){
				pathLogo = "hipercor";
			}else if(p.getUrlProducto().contains("carrefour")){
				pathLogo = "carrefour";
			}
			else{
				pathLogo = "amazon";
			}					
			
			//Aquí se muestran todos los productos;
			//Se muestra el precio y se realiza una llamada a la función esPrecioKilo() debido a que
			//los productos que almacenamos pueden aparecer en la fuente de datos de dos formas;
			// 1. Aparece solo con €.
			// 2. Aparece con €/kg. 
			//Como ordenamos los productos por precio, para poder realizar la ordenación, tenemos que quitar
			//tanto el signo del euro como el /kg. Antes de quitarlo, guardamos en una variable cómo era el formato
			//del precio para poder mostrarlo ahora de forma correcta.
			//El resto que se muestra es la información del producto.
			out.println("<li class='producto'><h3>"+p.getPrecio()+" "+p.esPrecioKilo()+
					"</h3><img class='logo-super' src='img/"+pathLogo+
					"-logo.jpg' alt='logo supermercado'><a href='"+
					p.getUrlProducto()+"' target='_blank'><img src='"+p.getUrlImagen()+
					"' alt='"+p.getProducto()+"'></a><p class='titulo'>"+p.getProducto()+
					"</p></li>");
		}
		
		%>
	</ul>
	
	<footer>
		<a href="mailto:e.danielterol@go.ugr.es">Contacto</a>
	</footer>

</body>
</html>