package usuarios;

public class Usuarios implements Serializer_Usuarios{
    long id_usuario;
    String nombre;
    String telefono;
    String email;
    String direccion;
    String tipo;

    public Usuarios(){
        this(0, "", "", "", "", "");
    }
    
    public Usuarios(Usuarios us){
        this.id_usuario=us.id_usuario;
        this.nombre=us.nombre;
        this.telefono=us.telefono;
        this.email=us.email;
        this.direccion=us.direccion;
        this.tipo=us.tipo;
    }

    public Usuarios(String data){
        deserialize(data);
    }

    public Usuarios(long id_usuario, String nombre, String telefono, String email, String direccion, String tipo){
        this.id_usuario=id_usuario;
        this.nombre=nombre;
        this.telefono=telefono;
        this.email=email;
        this.direccion=direccion;
        this.tipo=tipo;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nombre: %s, Teléfono: %s, Email: %s, Direccion: %s, Tipo: %s", this.id_usuario, this.nombre, this.telefono, this.email, this.direccion, this.tipo);
    }

    @Override
    public String serialize() {
        return String.format("\"%d\";\"%s\";\"%s\";\"%s\";\"%s\";\"%s\"", this.id_usuario, this.nombre, this.telefono, this.email, this.direccion, this.tipo);
    }

    private String substractQuotes(String data){
        return data.substring(1, data.length()-1);
    }

    @Override
    public void deserialize(String data) {
        String[] datos = data.split(";");
        this.id_usuario= Integer.parseInt(datos[0].substring(1, datos[0].length()-1));
        this.nombre= this.substractQuotes(datos[1]);
        this.telefono= this.substractQuotes(datos[2]);
        this.email= this.substractQuotes(datos[3]);
        this.direccion= this.substractQuotes(datos[4]);
        this.tipo= this.substractQuotes(datos[5]);
    }
}
