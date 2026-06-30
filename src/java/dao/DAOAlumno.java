package dao;

import modelo.Alumno;
import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOAlumno
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Alumno alumno;

    public ArrayList<Alumno> listar()
    {
        ArrayList<Alumno> list = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                alumno = new Alumno();
                alumno.setNL(rs.getInt("NL"));
                alumno.setNombre(rs.getString("Nombre"));
                alumno.setPaterno(rs.getString("Paterno"));
                alumno.setMaterno(rs.getString("Materno"));
                alumno.setDdi(rs.getInt("Ddi"));
                alumno.setDwi(rs.getInt("Dwi"));
                alumno.setEcbd(rs.getInt("Ecbd"));
                alumno.calcProm();
                list.add(alumno);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) { }
        return list;
    }

    public String mostrar()
    {
        String r, fila;

        r = """
                <br><br>
                <table border="0">
                    <caption>Lista de Alumnos</caption>
                    <thead>
                        <tr>
                            <th>NL</th>
                            <th>Nombre</th>
                            <th>Paterno</th>
                            <th>Materno</th>
                            <th>DDI</th>
                            <th>DWI</th>
                            <th>ECBD</th>
                            <th>Promedio</th>
                            <th colspan="2">Opciones</th>
                        </tr>
                    </thead>
                    <tbody>
                """;

        for (Alumno reg : listar())
        {
            fila = """
                        <tr>
                            <td> %d </td>
                            <td> %s </td>
                            <td> %s </td>
                            <td> %s </td>
                            <td> %d </td>
                            <td> %d </td>
                            <td> %d </td>
                            <td> %.2f </td>
                            <td>
                                <form method="post">
                                    <input type="hidden" name="accion" value="Editar"/>
                                    <input type="hidden" name="tfNL" value="%d"/>
                                    <input type="submit" value="Editar">
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <input type="hidden" name="accion" value="Eliminar"/>
                                    <input type="hidden" name="tfNL" value="%d"/>
                                    <input type="submit" value="Eliminar">
                                </form>
                            </td>
                        </tr>
                    """;
            r = r + String.format(fila, reg.getNL(), reg.getNombre(), reg.getPaterno(), reg.getMaterno(),
                    reg.getDdi(), reg.getDwi(), reg.getEcbd(), reg.getProm(), reg.getNL(), reg.getNL());
        }
        r = r + """
                    </tbody>
                </table>
                """;
        return r;
    }

    public Alumno buscar(int nL)
    {
        alumno = null;
        String sql = "SELECT * FROM alumnos WHERE NL = " + nL;
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                alumno = new Alumno();
                alumno.setNL(rs.getInt("NL"));
                alumno.setNombre(rs.getString("Nombre"));
                alumno.setPaterno(rs.getString("Paterno"));
                alumno.setMaterno(rs.getString("Materno"));
                alumno.setDdi(rs.getInt("Ddi"));
                alumno.setDwi(rs.getInt("Dwi"));
                alumno.setEcbd(rs.getInt("Ecbd"));
                alumno.calcProm();
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) { }
        return alumno;
    }

    public boolean agregar(Alumno alumno)
    {
        String sql="INSERT INTO alumnos VALUES(" +
                alumno.getNL() + "," +
                "'" + alumno.getNombre()  + "'," +
                "'" + alumno.getPaterno() + "'," +
                "'" + alumno.getMaterno() + "'," +
                alumno.getDdi() + "," +
                alumno.getDwi() + "," +
                alumno.getEcbd() + ")";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) { }
        return true;
    }

    public boolean editar(Alumno alumno, int old)
    {
        String sql="UPDATE alumnos SET " +
                " NL = " + alumno.getNL() + "," +
                " Nombre = '" + alumno.getNombre() + "'," +
                " Paterno = '" + alumno.getPaterno() + "'," +
                " Materno = '" + alumno.getMaterno() + "'," +
                " Ddi = " + alumno.getDdi() + "," +
                " Dwi = " + alumno.getDwi() + "," +
                " Ecbd = " + alumno.getEcbd() +
                " WHERE NL = " + old;
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

    public boolean eliminar(int nL)
    {
        String sql = "DELETE FROM alumnos WHERE NL = " + nL;
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