package usuarios;

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
import java.util.ArrayList;

import CRUD.CRUD;
import dataset.DataSetInterface;

public class UsuariosService implements CRUD<Usuarios>, DataSetInterface{
    Connection conn;
    public UsuariosService(Connection conn){
        this.conn=conn;
    }

    public ArrayList<Usuarios> requestAll() throws SQLException{
        Statement statement = null;
        ArrayList<Usuarios> result = new ArrayList<Usuarios>();
        statement = this.conn.createStatement();   
        String sql = "SELECT * FROM usuarios";
        ResultSet querySet = statement.executeQuery(sql);
        while(querySet.next()) {
            int id_usuario = querySet.getInt("id_usuario");
            String nombre = querySet.getString("nombre");
            String telefono = querySet.getString("telefono");
            String email = querySet.getString("email");
            String direccion = querySet.getString("direccion");
            String tipo = querySet.getString("tipo");
            result.add(new Usuarios(id_usuario, nombre, telefono, email, direccion, tipo));
        } 
        statement.close();    
        return result;
    }

    @Override
    public Usuarios requestById(long id) throws SQLException {
        Statement statement = null;
        Usuarios result = null;
        statement = this.conn.createStatement();
        String sql = String.format("SELECT * FROM usuarios WHERE id_usuario=%d", id);
        ResultSet querySet = statement.executeQuery(sql);
        if (querySet.next()) {
            String nombre = querySet.getString("nombre");
            String telefono = querySet.getString("telefono");
            String email = querySet.getString("email");
            String direccion = querySet.getString("direccion");
            String tipo = querySet.getString("tipo");
            result = new Usuarios(id, nombre, telefono, email, direccion, tipo);
        }
        statement.close();
        return result;
    }

    @Override
    public long create(Usuarios model) throws SQLException {
        String sqlaux = String.format("INSERT INTO usuarios (nombre, telefono, email, direccion, tipo) VALUES (?, ?, ?, ?, ?)");
        PreparedStatement prepst = this.conn.prepareStatement(sqlaux, Statement.RETURN_GENERATED_KEYS);
        prepst.setString(1, model.getNombre());
        prepst.setString(2, model.getTelefono());
        prepst.setString(3, model.getEmail());
        prepst.setString(4, model.getDireccion());
        prepst.setString(5, model.getTipo());
        prepst.execute();
        ResultSet keys = prepst.getGeneratedKeys();
        if (keys.next()) {
            long id_usuario = keys.getLong(1);
            prepst.close();
            return id_usuario;
        } else{
            throw new SQLException("Creating user failed, no rows affected.");
        }
    }
    @Override
    public int update(Usuarios object) throws SQLException {
        String sql = String.format("UPDATE usuarios SET nombre = '%s', telefono = '%s', email = '%s', direccion = '%s', tipo = '%s' WHERE id_usuario=%d", object.getNombre(), object.getTelefono(), object.getEmail(), object.getDireccion(), object.getTipo(), object.getId_usuario());
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
        String sql = String.format("DELETE FROM usuarios WHERE id_usuario=%d", id);
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
                Usuarios us = new Usuarios(line);
                String sql = "INSERT INTO usuarios (nombre, telefono, email, direccion, tipo) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), telefono=VALUES(telefono), email=VALUES(email), direccion=VALUES(direccion), tipo=VALUES(tipo)";
                prep = this.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                prep.setInt(1, (int)us.id_usuario);
                prep.setString(2, us.nombre);
                prep.setString(3, us.telefono);
                prep.setString(4, us.email);
                prep.setString(5, us.direccion);
                prep.setString(6, us.tipo);
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
            ArrayList<Usuarios> usuarios = this.requestAll();
            for(Usuarios us:usuarios){
                bw.write(us.serialize()+"\n");
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