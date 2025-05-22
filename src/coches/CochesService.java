package coches;

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

public class CochesService implements CRUD<Coches>, DataSetInterface{
    Connection conn;
    public CochesService(Connection conn){
        this.conn=conn;
    }

    public ArrayList<Coches> requestAll() throws SQLException{
        Statement statement = null;
        ArrayList<Coches> result = new ArrayList<Coches>();
        statement = this.conn.createStatement();   
        String sql = "SELECT * FROM coches";
        ResultSet querySet = statement.executeQuery(sql);
        while(querySet.next()) {
            int id_coche = querySet.getInt("id_coche");
            String marca = querySet.getString("marca");
            String modelo = querySet.getString("modelo");
            String precio = querySet.getString("precio");
            String kilometraje = querySet.getString("kilometraje");
            String tipo = querySet.getString("tipo");
            String estado = querySet.getString("estado");
            Long id_vendedor = querySet.getLong("id_vendedor");
            result.add(new Coches(id_coche, marca, modelo, precio, kilometraje, tipo, estado, id_vendedor));
        } 
        statement.close();    
        return result;
    }

    @Override
    public Coches requestById(long id) throws SQLException {
        Statement statement = null;
        Coches result = null;
        statement = this.conn.createStatement();
        String sql = String.format("SELECT * FROM coches WHERE id_coche=%d", id);
        ResultSet querySet = statement.executeQuery(sql);
        if (querySet.next()) {
            String marca = querySet.getString("marca");
            String modelo = querySet.getString("modelo");
            String precio = querySet.getString("precio");
            String kilometraje = querySet.getString("kilometraje");
            String tipo = querySet.getString("tipo");
            String estado = querySet.getString("estado");
            Long id_vendedor = querySet.getLong("id_vendedor")==0?null:querySet.getLong("id_vendedor");
            result = new Coches(id, marca, modelo, precio, kilometraje, tipo, estado, id_vendedor);
        }
        statement.close();
        return result;
    }

    @Override
    public long create(Coches model) throws SQLException {
        String sqlaux = String.format("INSERT INTO coches (marca, modelo, precio, kilometraje, tipo, estado, id_vendedor) VALUES (?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement prepst = this.conn.prepareStatement(sqlaux, Statement.RETURN_GENERATED_KEYS);
        prepst.setString(1, model.getMarca());
        prepst.setString(2, model.getModelo());
        prepst.setString(3, model.getPrecio());
        prepst.setString(4, model.getKilometraje());
        prepst.setString(5, model.getTipo());
        prepst.setString(6, model.getEstado());
        prepst.setLong(7, model.getId_vendedor());
        prepst.execute();
        ResultSet keys = prepst.getGeneratedKeys();
        if (keys.next()) {
            long id_coche = keys.getLong(1);
            prepst.close();
            return id_coche;
        } else{
            throw new SQLException("Creating user failed, no rows affected.");
        }
    }

    @Override
    public int update(Coches object) throws SQLException {
        String sql = String.format("UPDATE coches SET marca = '%s', modelo = '%s', precio = '%s', kilometraje = '%s', tipo = '%s', estado = '%s', id_vendedor = %d WHERE id_coche=%d", object.getMarca(), object.getModelo(), object.getPrecio(), object.getKilometraje(), object.getTipo(), object.getEstado(), object.getId_vendedor(), object.getId_coche());
        PreparedStatement prepstat = this.conn.prepareStatement(sql);
        int affectedRows = prepstat.executeUpdate();
        if (affectedRows == 0)
            throw new SQLException("Updating user failed, no rows affected.");
        else
            return affectedRows;
    }

    @Override
    public boolean delete(long id) throws SQLException {
        Statement statement = null;
        statement = this.conn.createStatement();    
        String sql = String.format("DELETE FROM coches WHERE id_coche=%d", id);
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
                Coches co = new Coches(line);
                String sql = "INSERT INTO coches (id_coche, marca, modelo, precio, kilometraje, tipo, estado, id_vendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE marca=VALUES(marca), modelo=VALUES(modelo), precio=VALUES(precio), kilometraje=VALUES(kilometraje), tipo=VALUES(tipo), estado=VALUES(estado), id_vendedor=VALUES(id_vendedor)";
                prep = this.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                prep.setInt(1, (int)co.id_coche);
                prep.setString(2, co.marca);
                prep.setString(3, co.modelo);
                prep.setString(4, co.precio);
                prep.setString(5, co.kilometraje);
                prep.setString(6, co.tipo);
                prep.setString(7, co.estado);
                if(co.id_vendedor!=null)
                    prep.setInt(8, (int)co.id_vendedor.longValue());
                else
                    prep.setNull(8, Types.INTEGER);
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
            ArrayList<Coches> coches = this.requestAll();
            for(Coches co:coches){
                bw.write(co.serialize()+"\n");
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
