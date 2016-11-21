package ufps.edu.co.appnotiufps;

/**
 * Created by omara on 16/11/2016.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PruebaBD {
    private Connection conexion;
    private String DRIVER = "com.mysql.jdbc.Driver";
    private String dBUrl = "jdbc:mysql://sandbox2.ufps.edu.co/1151211";
    private String user = "1151211";
    private String pass = "1151211";

    public PruebaBD() {

    }

    private boolean crearConexionSQL(){
        boolean entro=false;
        try{
            Class.forName(DRIVER).newInstance();
            //Get a connection
            this.conexion = DriverManager.getConnection(dBUrl, user, pass);
            entro=true;
        }catch (Exception e){
            entro=false;
            e.printStackTrace();
        }
        return entro;
    }

    public ArrayList<Evento> listarAllEventos() throws Exception {
        this.crearConexionSQL();
        ArrayList<Evento>array=new ArrayList<>();

        try {
            //String con la consulta sql
            String selectStatement = "SELECT * from eventos";
            //prepara la consulta sql
            PreparedStatement prepStmt = this.conexion.prepareStatement(selectStatement);
            //ejecuta la consulta sql y devuelve las filas de la base de datos
            ResultSet row = prepStmt.executeQuery();
            while (row.next()) {
                    Evento evento=new Evento(row.getString("title"),row.getString("body"),
                            row.getString("url"), row.getString("class"), row.getString("inicio_normal"),
                            row.getString("final_normal"),row.getString("url_imagen"));

                    array.add(evento);
            }
            row.close();

        } catch (Exception e) {
            array=null;
            throw new Exception(e);
        } finally {
            this.cerrarConexion();
        }
        return array;
    }

    public boolean cerrarConexion() throws Exception {
        if (this.conexion != null) {
            this.conexion.close();
        }
        return true;
    }

}
