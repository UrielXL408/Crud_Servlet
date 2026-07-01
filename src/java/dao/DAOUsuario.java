package dao;

import modelo.Usuario;
import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DAOUsuario
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Usuario usuario;

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
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) { }
        return usuario;
    }

    // Valida correo + contraseña (en texto plano) para el login.
    // El hash se calcula aqui adentro, antes de comparar contra la BD.
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
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) { }
        return usuario;
    }

    // Recibe el usuario con la contraseña en texto plano y la encripta antes de guardar
    public boolean agregar(Usuario usuario)
    {
        String contrasenaHash = encriptar(usuario.getContrasena());

        String sql = "INSERT INTO usuario (Nombre, APaterno, AMaterno, Correo, Contrasena, Estado) VALUES(" +
                "'" + usuario.getNombre()   + "'," +
                "'" + usuario.getAPaterno() + "'," +
                "'" + usuario.getAMaterno() + "'," +
                "'" + usuario.getCorreo()   + "'," +
                "'" + contrasenaHash        + "'," +
                "'" + usuario.getEstado()   + "')";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();
        }
        catch (SQLException e) { }
        return true;
    }
}