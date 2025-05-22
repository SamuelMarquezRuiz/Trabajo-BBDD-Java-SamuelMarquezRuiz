package inventario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import CRUD.CRUD;
import dataset.DataSetInterface;

public class InventarioService implements CRUD<Inventario>, DataSetInterface{
    Connection conn;
    public InventarioService(Connection conn){
        this.conn=conn;
    }

    public ArrayList<Inventario> requestAll() throws SQLException{
        Statement statement = null;
        ArrayList<Inventario> result = new ArrayList<Inventario>();
        statement = this.conn.createStatement();   
        String sql = "SELECT * FROM inventario";
        ResultSet querySet = statement.executeQuery(sql);
        while(querySet.next()) {
            int id_inventario = querySet.getInt("id_inventario");
            String estado = querySet.getString("estado");
            Long id_coche = querySet.getLong("id_coche");
            result.add(new Inventario(id_inventario, estado, id_coche));
        } 
        statement.close();    
        return result;
    }

    @Override
    public Inventario requestById(long id) throws SQLException {
        Statement statement = null;
        Inventario result = null;
        statement = this.conn.createStatement();
        String sql = String.format("SELECT * FROM inventario WHERE id_inventario=%d", id);
        ResultSet querySet = statement.executeQuery(sql);
        if (querySet.next()) {
            String estado = querySet.getString("estado");
            Long id_coche = querySet.getLong("id_coche")==0?null:querySet.getLong("id_coche");
            result = new Inventario(id, estado, id_coche);
        }
        statement.close();
        return result;
    }

    @Override
    public long create(Inventario model) throws SQLException {
        String sqlaux = String.format("INSERT INTO inventario (estado, id_coche) VALUES (?, ?)");
        PreparedStatement prepst = this.conn.prepareStatement(sqlaux, Statement.RETURN_GENERATED_KEYS);
        prepst.setString(1, model.getEstado());
        prepst.setLong(2, model.getId_coche());
        prepst.execute();
        ResultSet keys = prepst.getGeneratedKeys();
        if (keys.next()) {
            long id_inventario = keys.getLong(1);
            prepst.close();
            return id_inventario;
        } else{
            throw new SQLException("Creating user failed, no rows affected.");
        }
    }
    
    @Override
    public int update(Inventario object) throws SQLException {  
        String sql = String.format("UPDATE inventario SET estado = '%s', id_coche = %d WHERE id_inventario=%d", object.getEstado(), object.getId_coche(), object.getId_inventario());
        PreparedStatement prepst = this.conn.prepareStatement(sql);
        int affectedRows = prepst.executeUpdate();
        if (affectedRows == 0)
            throw new SQLException("Creating user failed, no rows affected.");
        else
            return affectedRows;
    }

    @Override
    public boolean delete(long id) throws SQLException {
        Statement statement = null;
        statement = this.conn.createStatement();    
        String sql = String.format("DELETE FROM inventario WHERE id_inventario=%d", id);
        // Ejecución de la consulta
        int result = statement.executeUpdate(sql);
        statement.close();
        return result==1;
    }

    @Override
    public void importFromCSV(String file) throws Exception {
        BufferedReader br = null;
        PreparedStatement prep = null;
        try {
            br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            String line = "";
            while((line=br.readLine())!= null){
                Inventario in = new Inventario(line);
                String sql = "INSERT INTO inventario (id_inventario, estado, id_coche) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE estado=VALUES(estado), id_coche=VALUES(id_coche)";
                prep = this.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                prep.setInt(1, (int)in.id_inventario);
                prep.setString(2, in.estado);
                if(in.id_coche!=null)
                    prep.setInt(3, (int)in.id_coche.longValue());
                else
                    prep.setNull(3, Types.INTEGER);
                prep.execute();
            }    
        } catch (IOException e) {
            throw new Exception("Ocurrión un error de E/S"+ e.toString());
        } catch (SQLTimeoutException e){
            throw new Exception("Ocurrión un error al acceder a la base de datos"+ e.toString());
        } catch (SQLException e){
            throw new Exception("Ocurrión un error al acceder a la base de datos"+ e.toString());
        } catch (Exception e){
            throw new Exception("Ocurrión un error "+ e.toString());
        } finally {
            if(prep != null)
                prep.close();
            if(br != null)
                br.close();
        }
    }

    @Override
    public void exportToCSV(String file) throws Exception {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
            ArrayList<Inventario> inventario = this.requestAll();
            for(Inventario in:inventario){
                bw.write(in.serialize()+"\n");
            }
            bw.close();
        } catch(IOException e){
            throw new Exception("Ocurrión un error de E/S "+ e.toString());
        } catch(SQLException e){
            throw new Exception("Ocurrión un error al acceder a la base de datos "+ e.toString());
        }catch (Exception e) {
            throw new Exception("Ocurrión un error "+ e.toString());
        } finally {
            if(bw!=null)
                bw.close();
        }
    }
}
