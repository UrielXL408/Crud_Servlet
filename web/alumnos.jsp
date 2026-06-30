<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Alumno"%>
<%@page import="dao.DAOAlumno"%>

<%!
    DAOAlumno lista = new DAOAlumno();
    Alumno edit = null;
%>

<%
    edit = null;
    if (request.getAttribute("edit") != null)
    {
        edit = (Alumno) request.getAttribute("edit");
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BD con Servlets</title>
        <link rel="stylesheet" href="estilo.css">
    </head>
    <body>
        <div id="form_registro">
            <h2><%= (edit != null) ? "Modificar calificaciones" : "Registro de calificaciones"%></h2>
            <form method="post">
                <input type="hidden" name="accion"   id="accion"   value="<%= (edit != null) ? "Modificar" : "Agregar"%>" />
                <input type="hidden" name="tfNLOld"  id="tfNLOld"  value="<%= (edit != null) ? edit.getNL() : ""%>" />
                <input type="text"   name="tfNL"     id="tfNL"     value="<%= (edit != null) ? edit.getNL() : ""%>"       placeholder="Numero de Lista"       required />
                <input type="text"   name="tfNombre" id="tfNombre" value="<%= (edit != null) ? edit.getNombre() : ""%>"   placeholder="Nombre"   required />
                <input type="text"   name="tfPaterno" id="tfPaterno" value="<%= (edit != null) ? edit.getPaterno() : ""%>" placeholder="Apellido Paterno"  required />
                <input type="text"   name="tfMaterno" id="tfMaterno" value="<%= (edit != null) ? edit.getMaterno() : ""%>" placeholder="Apellido Materno"  required />
                <input type="text"   name="tfDdi"     id="tfDdi"     value="<%= (edit != null) ? edit.getDdi() : ""%>"     placeholder="Desarrollo de Dispositivos Inteligentes"      required />
                <input type="text"   name="tfDwi"     id="tfDwi"     value="<%= (edit != null) ? edit.getDwi() : ""%>"     placeholder="Desarrollo Web Integral"      required />
                <input type="text"   name="tfEcbd"    id="tfEcbd"    value="<%= (edit != null) ? edit.getEcbd() : ""%>"    placeholder="Extraccion del Conocimiento de Base de Datos"     required />
                <input type="submit" value="<%= (edit != null) ? "Modificar" : "Agregar"%>" />
            </form>
        </div>
            
        <%=lista.mostrar()%>
    </body>
</html>