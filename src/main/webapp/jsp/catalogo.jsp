<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>PR01 - Catalogo de productos (Web 1.0)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
<header>
    <h1>PR01. Control de inventario inteligente</h1>
    <nav aria-label="Navegacion principal">
        <a href="${pageContext.request.contextPath}/catalogo">Catalogo (Web 1.0)</a> |
        <a href="${pageContext.request.contextPath}/movimiento">Movimientos (Web 1.0)</a> |
        <a href="${pageContext.request.contextPath}/producto">Nuevo producto</a> |
        <a href="${pageContext.request.contextPath}/faces/productos.xhtml">Panel Web 2.0 (JSF/PrimeFaces)</a>
    </nav>
</header>

<main>
    <h2>Catalogo de productos</h2>
    <p>
        <a href="${pageContext.request.contextPath}/catalogo">Ver todos</a> |
        <a href="${pageContext.request.contextPath}/catalogo?alerta=1">Ver solo en alerta (existencia &lt; minimo)</a>
    </p>

    <table>
        <caption>${soloAlerta ? 'Productos con existencia por debajo del minimo' : 'Todos los productos activos'}</caption>
        <thead>
        <tr>
            <th scope="col">Codigo</th>
            <th scope="col">Nombre</th>
            <th scope="col">Categoria</th>
            <th scope="col">Unidad</th>
            <th scope="col">Existencia</th>
            <th scope="col">Minimo</th>
            <th scope="col">Estado</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${productos}">
            <tr class="${p.alertaStockBajo ? 'fila-alerta' : ''}">
                <td>${p.codigo}</td>
                <td>${p.nombre}</td>
                <td>${p.nombreCategoria}</td>
                <td>${p.unidadMedida}</td>
                <td><fmt:formatNumber value="${p.existencia}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${p.minimoPermitido}" maxFractionDigits="2"/></td>
                <td>
                    <c:choose>
                        <c:when test="${p.alertaStockBajo}"><strong>ALERTA: stock bajo</strong></c:when>
                        <c:otherwise>Normal</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty productos}">
            <tr><td colspan="7">No hay productos que mostrar.</td></tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>
