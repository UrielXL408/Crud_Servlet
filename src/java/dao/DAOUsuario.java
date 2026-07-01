package dao;

import modelo.Usuario;
import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.Properties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class DAOUsuario
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Usuario usuario;

    // ====== DATOS DE TU CUENTA DE GMAIL (cambialos por los tuyos) ======
    private static final String EMAIL_REMITENTE = "axelurielramoslopez@gmail.com";
    private static final String CLAVE_REMITENTE  = "ixhzsyhlgdchlwib"; // App Password de Gmail, no tu contraseña normal
    private static final String URL_BASE = "http://localhost:8080/Crud_Servlet"; // ajusta si tu contexto es otro
    // =====================================================================

    // Convierte un texto plano en su hash SHA-256 (en hexadecimal)
    private String encriptar(String textoPlano)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(textoPlano.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e)
        {
            return null;
        }
    }

    // Envia el correo de verificacion con el link que contiene el token
    private void enviarCorreoVerificacion(String destinatario, String token)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(EMAIL_REMITENTE, CLAVE_REMITENTE);
            }
        });

        try
        {
            String link = URL_BASE + "/Servlet_Login?accion=Verificar&token=" + token;

            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(EMAIL_REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Verifica tu cuenta");
            mensaje.setText("Hola, para activar tu cuenta entra a este link:\n\n" + link);

            Transport.send(mensaje);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }

    // Busca un usuario por correo, sin validar contraseña (sirve para saber si ya existe)
    public Usuario buscarPorCorreo(String correo)
    {
        usuario = null;
        String sql = "SELECT * FROM usuario WHERE Correo = '" + correo + "'";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                usuario = new Usuario();
                usuario.setId(rs.getInt("Id"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setAPaterno(rs.getString("APaterno"));
                usuario.setAMaterno(rs.getString("AMaterno"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setContrasena(rs.getString("Contrasena"));
                usuario.setEstado(rs.getString("Estado"));
                usuario.setToken(rs.getString("Token"));
                usuario.setVerificado(rs.getString("Verificado"));
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) { }
        return usuario;
    }

    // Valida correo + contraseña (en texto plano) para el login.
    public Usuario login(String correo, String contrasenaPlano)
    {
        usuario = null;
        String contrasenaHash = encriptar(contrasenaPlano);
        String sql = "SELECT * FROM usuario WHERE Correo = '" + correo + "' AND Contrasena = '" + contrasenaHash + "'";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                usuario = new Usuario();
                usuario.setId(rs.getInt("Id"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setAPaterno(rs.getString("APaterno"));
                usuario.setAMaterno(rs.getString("AMaterno"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setContrasena(rs.getString("Contrasena"));
                usuario.setEstado(rs.getString("Estado"));
                usuario.setToken(rs.getString("Token"));
                usuario.setVerificado(rs.getString("Verificado"));
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) { }
        return usuario;
    }

    // Registra al usuario con su contraseña ya hasheada, token generado y Verificado='No',
    // y le manda el correo con el link de verificacion
    public boolean agregar(Usuario usuario)
    {
        String contrasenaHash = encriptar(usuario.getContrasena());
        String token = UUID.randomUUID().toString();

        String sql = "INSERT INTO usuario (Nombre, APaterno, AMaterno, Correo, Contrasena, Estado, Token, Verificado) VALUES(" +
                "'" + usuario.getNombre()   + "'," +
                "'" + usuario.getAPaterno() + "'," +
                "'" + usuario.getAMaterno() + "'," +
                "'" + usuario.getCorreo()   + "'," +
                "'" + contrasenaHash        + "'," +
                "'" + usuario.getEstado()   + "'," +
                "'" + token                 + "'," +
                "'No')";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();

            enviarCorreoVerificacion(usuario.getCorreo(), token);
        }
        catch (SQLException e) { return false; }
        return true;
    }

    // Marca como verificado al usuario dueño de ese token. Retorna true si encontro y actualizo
    public boolean verificarToken(String token)
    {
        boolean actualizado = false;
        String sqlBuscar = "SELECT Id FROM usuario WHERE Token = '" + token + "' AND Verificado = 'No'";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sqlBuscar);
            rs = ps.executeQuery();

            if (rs.next())
            {
                rs.close();
                ps.close();

                String sqlUpdate = "UPDATE usuario SET Verificado = 'Si' WHERE Token = '" + token + "'";
                ps = con.prepareStatement(sqlUpdate);
                ps.executeUpdate();
                actualizado = true;
                ps.close();
            }
            else
            {
                rs.close();
                ps.close();
            }
            con.close();
        }
        catch (SQLException e) { }
        return actualizado;
    }
}