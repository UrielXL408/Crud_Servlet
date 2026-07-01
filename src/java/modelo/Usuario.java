package modelo;

public class Usuario
{
    private int id;
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String correo;
    private String contrasena;
    private String estado;

    public Usuario()
    {
        id = 0;
        nombre = "Sin nombre";
        aPaterno = "Sin paterno";
        aMaterno = "Sin materno";
        correo = "";
        contrasena = "";
        estado = "Pendiente";
    }

    public Usuario(String nombre, String aPaterno, String aMaterno, String correo, String contrasena)
    {
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.correo = correo;
        this.contrasena = contrasena;
        this.estado = "Pendiente";
    }

    public Usuario(int id, String nombre, String aPaterno, String aMaterno, String correo, String contrasena, String estado)
    {
        this.id = id;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.correo = correo;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public int getId()
    {
        return id;
    }
    public String getNombre()
    {
        return nombre;
    }
    public String getAPaterno()
    {
        return aPaterno;
    }
    public String getAMaterno()
    {
        return aMaterno;
    }
    public String getCorreo()
    {
        return correo;
    }
    public String getContrasena()
    {
        return contrasena;
    }
    public String getEstado()
    {
        return estado;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
    public void setAPaterno(String aPaterno)
    {
        this.aPaterno = aPaterno;
    }
    public void setAMaterno(String aMaterno)
    {
        this.aMaterno = aMaterno;
    }
    public void setCorreo(String correo)
    {
        this.correo = correo;
    }
    public void setContrasena(String contrasena)
    {
        this.contrasena = contrasena;
    }
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
}