<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>PR01 - Nuevo producto (Web 1.0)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
<header>
    <h1>PR01. Control de inventario inteligente</h1>
    <nav aria-label="Navegacion principal">
        <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a> |
        <a href="${pageContext.request.contextPath}/movimiento">Movimientos</a>
    </nav>
</header>
<main>
    <h2>Registrar nuevo producto</h2>

    <c:if test="${not empty error}">
        <p role="alert" class="mensaje-error">${error}</p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/producto">
        <div>
            <label for="codigo">Codigo</label>
            <input type="text" id="codigo" name="codigo" required>
        </div>
        <div>
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre" required>
        </div>
        <div>
            <label for="idCategoria">Categoria</label>
            <select id="idCategoria" name="idCategoria" required>
                <c:forEach var="cat" items="${categorias}">
                    <option value="${cat.idCategoria}">${cat.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <div>
            <label for="unidadMedida">Unidad de medida</label>
            <input type="text" id="unidadMedida" name="unidadMedida" value="pieza" required>
        </div>
        <div>
            <label for="minimoPermitido">Minimo permitido</label>
            <input type="number" id="minimoPermitido" name="minimoPermitido" min="0" step="0.01" value="0" required>
        </div>
        <button type="submit">Guardar producto</button>
    </form>
</main>
</body>
</html>
