<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>PR01 - Movimientos de inventario (Web 1.0)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
<header>
    <h1>PR01. Control de inventario inteligente</h1>
    <nav aria-label="Navegacion principal">
        <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a> |
        <a href="${pageContext.request.contextPath}/movimiento">Movimientos</a> |
        <a href="${pageContext.request.contextPath}/producto">Nuevo producto</a>
    </nav>
</header>
<main>
    <h2>Registrar entrada o salida</h2>

    <c:if test="${not empty mensaje}">
        <p role="status" class="mensaje-ok">${mensaje}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p role="alert" class="mensaje-error">${error}</p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/movimiento">
        <div>
            <label for="idProducto">Producto</label>
            <select id="idProducto" name="idProducto" required>
                <c:forEach var="p" items="${productos}">
                    <option value="${p.idProducto}">${p.codigo} - ${p.nombre} (existencia: ${p.existencia})</option>
                </c:forEach>
            </select>
        </div>
        <div>
            <label for="idUsuario">Usuario que registra</label>
            <select id="idUsuario" name="idUsuario" required>
                <c:forEach var="u" items="${usuarios}">
                    <option value="${u.idUsuario}">${u.nombre} (${u.rol})</option>
                </c:forEach>
            </select>
        </div>
        <div>
            <label for="tipo">Tipo de movimiento</label>
            <select id="tipo" name="tipo" required>
                <option value="ENTRADA">Entrada</option>
                <option value="SALIDA">Salida</option>
            </select>
        </div>
        <div>
            <label for="cantidad">Cantidad</label>
            <input type="number" id="cantidad" name="cantidad" min="0.01" step="0.01" required>
        </div>
        <div>
            <label for="comentario">Comentario (opcional)</label>
            <input type="text" id="comentario" name="comentario">
        </div>
        <button type="submit">Registrar movimiento</button>
    </form>

    <h3>Ultimos movimientos</h3>
    <table>
        <thead>
        <tr>
            <th scope="col">Fecha</th>
            <th scope="col">Producto</th>
            <th scope="col">Usuario</th>
            <th scope="col">Tipo</th>
            <th scope="col">Cantidad</th>
            <th scope="col">Comentario</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="m" items="${recientes}">
            <tr>
                <td>${m.fechaHora}</td>
                <td>${m.nombreProducto}</td>
                <td>${m.nombreUsuario}</td>
                <td>${m.tipo}</td>
                <td>${m.cantidad}</td>
                <td>${m.comentario}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty recientes}">
            <tr><td colspan="6">Aun no hay movimientos registrados.</td></tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>
