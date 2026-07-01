package control;

import modelo.Usuario;
import dao.DAOUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Servlet_Login", urlPatterns = {"/Servlet_Login"})
public class Servlet_Login extends HttpServlet
{
    private Usuario usuario;
    private DAOUsuario dao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html;charset=UTF-8");
        String mensaje = null;

        try
        {
            String accion = request.getParameter("accion");

            if ("Registrar".equals(accion))
            {
                dao = new DAOUsuario();

                String correo = request.getParameter("tfCorreo");

                if (dao.buscarPorCorreo(correo) != null)
                {
                    mensaje = "Ese correo ya esta registrado.";
                }
                else
                {
                    usuario = new Usuario();
                    usuario.setNombre(request.getParameter("tfNombre"));
                    usuario.setAPaterno(request.getParameter("tfAPaterno"));
                    usuario.setAMaterno(request.getParameter("tfAMaterno"));
                    usuario.setCorreo(correo);

                    String contrasenaPlano = request.getParameter("tfContrasena");
                    usuario.setContrasena(contrasenaPlano);

                    usuario.setEstado("Aprobado");

                    dao.agregar(usuario);
                    mensaje = "Registro exitoso. Ya puedes iniciar sesion.";
                }

                request.setAttribute("mensaje", mensaje);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
            }
            else if ("Login".equals(accion))
            {
                dao = new DAOUsuario();

                String correo = request.getParameter("tfCorreoLogin");
                String contrasenaPlano = request.getParameter("tfContrasenaLogin");

                usuario = dao.login(correo, contrasenaPlano);

                if (usuario == null)
                {
                    mensaje = "Correo o contrasena incorrectos.";
                    request.setAttribute("mensaje", mensaje);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                }
                else if ("Pendiente".equals(usuario.getEstado()))
                {
                    mensaje = "Tu cuenta aun no ha sido aprobada.";
                    request.setAttribute("mensaje", mensaje);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                }
                else if ("Inhabilitado".equals(usuario.getEstado()))
                {
                    mensaje = "Tu cuenta esta inhabilitada.";
                    request.setAttribute("mensaje", mensaje);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                }
                else // Aprobado
                {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario);
                    response.sendRedirect(request.getContextPath() + "/Servlet_Alumno");
                }
            }
            else //mostrar por default
            {
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
            }
        }
        catch (IOException | ServletException ex)
        {
            Logger.getLogger(Servlet_Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "Short description";
    }
}