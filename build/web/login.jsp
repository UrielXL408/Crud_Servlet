<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String mensaje = null;
    if (request.getAttribute("mensaje") != null)
    {
        mensaje = (String) request.getAttribute("mensaje");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Iniciar sesion</title>
        <link rel="stylesheet" href="estilo1.css">
    </head>
    <body>
        <% if (mensaje != null) { %>
        <p><%= mensaje %></p>
        <% } %>

        <div id="form_registro">
            <h2>Iniciar sesion</h2>
            <form method="post" action="Servlet_Login">
                <input type="hidden" name="accion" id="accion" value="Login" />
                <input type="email" name="tfCorreoLogin" id="tfCorreoLogin" placeholder="Correo" required />
                <input type="password" name="tfContrasenaLogin" id="tfContrasenaLogin" placeholder="Contrasena" required />
                <input type="submit" value="Ingresar" />
            </form>
        </div>

        <div id="form_registro">
            <h2>Registro de cuenta</h2>
            <form method="post" action="Servlet_Login">
                <input type="hidden" name="accion" id="accion2" value="Registrar" />
                <input type="text"  name="tfNombre"     id="tfNombre"     placeholder="Nombre"            required />
                <input type="text"  name="tfAPaterno"   id="tfAPaterno"   placeholder="Apellido Paterno"  required />
                <input type="text"  name="tfAMaterno"   id="tfAMaterno"   placeholder="Apellido Materno"  required />
                <input type="email" name="tfCorreo"     id="tfCorreo"     placeholder="Correo"            required />
                <input type="password" name="tfContrasena" id="tfContrasena" placeholder="Contrasena"     required />
                <input type="submit" value="Registrarme" />
            </form>
        </div>
    </body>
</html>