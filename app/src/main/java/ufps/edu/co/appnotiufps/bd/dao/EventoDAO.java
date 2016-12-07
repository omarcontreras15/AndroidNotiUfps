package ufps.edu.co.appnotiufps.bd.dao;

/**
 * Created by omara on 16/11/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.Switch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import ufps.edu.co.appnotiufps.R;
import ufps.edu.co.appnotiufps.bd.dto.EventoDTO;

public class EventoDAO {
    private Connection conexion;
    private String DRIVER = "com.mysql.jdbc.Driver";
    private String dBUrl = "jdbc:mysql://sandbox2.ufps.edu.co/1151211";
    private String user = "1151211";
    private String pass = "1151211";

    public EventoDAO() {

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

    public ArrayList<EventoDTO> listarAllEventos() throws Exception {
        this.crearConexionSQL();
        ArrayList<EventoDTO>array=new ArrayList<>();

        try {
            //String con la consulta sql
            String consulta = "SELECT * from eventos";
            //prepara la consulta sql
            PreparedStatement prepStmt = this.conexion.prepareStatement(consulta);
            //ejecuta la consulta sql y devuelve las filas de la base de datos
            ResultSet row = prepStmt.executeQuery();
            while (row.next()) {
                    EventoDTO eventoDTO =new EventoDTO(row.getInt("id"),row.getString("title"),row.getString("body"),
                            row.getString("url"), row.getString("class"), row.getString("inicio_normal"),
                            row.getString("final_normal"),row.getString("url_imagen"));

                    array.add(eventoDTO);
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

    public ArrayList<EventoDTO>listarEventosNotificaciones(Context ctx) throws Exception {

        ArrayList<EventoDTO>array=new ArrayList<>();

        SharedPreferences config = ctx.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        boolean academico = config.getBoolean("checkAcademico", false);
        boolean cultural = config.getBoolean("checkCultural", false);
        boolean deportivo = config.getBoolean("checkDeportivo", false);
        boolean congresos= config.getBoolean("checkCongresos", false);
        boolean empresarial= config.getBoolean("checkEmpresarial", false);
        boolean administrativo=config.getBoolean("checkAdministrativo",false);
        boolean ciencia_tecnologia= config.getBoolean("checkCiencia_Tecnologia", false);

        //verificamos si por lo menos alguno de los filtros esta activado
        if(academico || cultural || deportivo || congresos || empresarial || administrativo ||ciencia_tecnologia){
            String consulta="SELECT * from eventos where";
            if(academico){
                consulta+=" class='Academico' or";
            }
            if(cultural){
                consulta+=" class='Cultural' or";
            }
            if(deportivo){
                consulta+=" class='Deportivo' or";
            }
            if(congresos){
                consulta+=" class='Congresos' or";
            }
            if(empresarial){
                consulta+=" class='Empresarial' or";
            }
            if(administrativo){
                consulta+=" class='Administrativo' or";
            }
            if(ciencia_tecnologia){
                consulta+=" class='Ciencia-Tecnologia' or";
            }
            //eliminamos el or de la ultima condicion
            consulta=consulta.substring(0,consulta.length()-3);

            try {
                //crea la conexion a la base de datos
                this.crearConexionSQL();
                //prepara la consulta sql
                PreparedStatement prepStmt = this.conexion.prepareStatement(consulta);
                //ejecuta la consulta sql y devuelve las filas de la base de datos
                ResultSet row = prepStmt.executeQuery();
                while (row.next()) {

                    //Verfica que la fecha actual sea menor que la fecha de finalizacion del evento
                    if(new Date().getTime()<row.getLong("end")){
                        EventoDTO eventoDTO =new EventoDTO(row.getInt("id"),row.getString("title"),row.getString("body"),
                                row.getString("url"), row.getString("class"), row.getString("inicio_normal"),
                                row.getString("final_normal"),row.getString("url_imagen"));

                        array.add(eventoDTO);

                    }
                }
                //cierra el iterador de filas
                row.close();

            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                this.cerrarConexion();
            }

        }
        return array;
    }

    //Este metodo sirve para cerrar la conexion con la base de datos
    private boolean cerrarConexion() throws Exception {
        if (this.conexion != null) {
            this.conexion.close();
        }
        return true;
    }

}
