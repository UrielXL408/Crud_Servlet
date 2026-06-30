package modelo;
public class Alumno
{
    private int nL;
    private String nombre;
    private String paterno;
    private String materno;
    private int ddi;
    private int dwi;
    private int ecbd;
    private double prom;

    public Alumno()
    {
        nL = 0;
        nombre = "Sin nombre";
        paterno = "Sin paterno";
        materno = "Sin materno";
        ddi = dwi = ecbd = 0;
        prom = 0.0;
    }

    public Alumno(int nL, String nombre, String paterno, String materno)
    {
        this.nL = nL;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        ddi = dwi = ecbd = 0;
        prom = 0.0;
    }

    public Alumno(int nL, String nombre, String paterno, String materno, int ddi, int dwi, int ecbd)
    {
        this.nL = nL;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.ddi = ddi;
        this.dwi = dwi;
        this.ecbd = ecbd;
        prom = (ddi + dwi + ecbd) / 3.0;
    }

    public int getNL()
    {
        return nL;
    }
    public String getNombre()
    {
        return nombre;
    }
    public String getPaterno()
    {
        return paterno;
    }
    public String getMaterno()
    {
        return materno;
    }
    public int getDdi()
    {
        return ddi;
    }
    public int getDwi()
    {
        return dwi;
    }
    public int getEcbd()
    {
        return ecbd;
    }
    public double getProm()
    {
        return prom;
    }

    public void setNL(int nL)
    {
        this.nL = nL;
    }
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
    public void setPaterno(String paterno)
    {
        this.paterno = paterno;
    }   
    public void setMaterno(String materno)
    {
        this.materno = materno;
    }
    public void setDdi(int ddi)
    {
        this.ddi = ddi;
    }
    public void setDwi(int dwi)
    {
        this.dwi = dwi;
    }
    public void setEcbd(int ecbd)
    {
        this.ecbd = ecbd;
    }

    public double calcProm()
    {
        prom = (ddi + dwi + ecbd) / 3.0;
        return prom;
    }
}