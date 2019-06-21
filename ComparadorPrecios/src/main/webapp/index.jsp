<%@page import="java.util.ArrayList"%>
<%@page import="comparadorPrecios.Scraping"%>
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
	

	<footer class="pie-pag">
		<a href="mailto:e.danielterol@go.ugr.es">Contacto</a>
	</footer>

</body>
</html>