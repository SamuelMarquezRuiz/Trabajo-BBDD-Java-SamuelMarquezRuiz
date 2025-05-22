package coches;

public class Coches implements Serializer_Coches{
    long id_coche;
    String marca;
    String modelo;
    String precio;
    String kilometraje;
    String tipo;
    String estado;
    Long id_vendedor;

    public Coches(){
        this(0,"","","","","","",null);
    }

    public Coches(Coches co){
        this.id_coche=co.id_coche;
        this.marca=co.marca;
        this.modelo=co.modelo;
        this.precio=co.precio;
        this.kilometraje=co.kilometraje;
        this.tipo=co.tipo;
        this.estado=co.estado;
        this.id_vendedor=co.id_vendedor;
    }

    public Coches(String data){
        deserialize(data);
    }

    public Coches(long id_coche, String marca, String modelo, String precio, String kilometraje, String tipo, String estado, Long id_vendedor){
            this.id_coche= id_coche;
            this.marca= marca;
            this.modelo= modelo;
            this.precio= precio;
            this.kilometraje= kilometraje;
            this.tipo= tipo;
            this.estado= estado;
            this.id_vendedor= id_vendedor;
    }

    public long getId_coche() {
        return id_coche;
    }

    public void setId_coche(long id_coche) {
        this.id_coche = id_coche;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(long id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Marca: %s, Modelo: %s, Precio: %s, Kilometraje: %s, Tipo: %s, Estado: %s",this.id_coche,this.marca,this.modelo,this.precio,this.kilometraje,this.tipo,this.estado);
    }

    @Override
    public String serialize() {
        return String.format("\"%d\";\"%s\";\"%s\";\"%s\";\"%s\";\"%s\";\"%s", this.id_coche, this.marca, this.modelo, this.precio, this.kilometraje, this.tipo, this.estado, this.id_vendedor!=null?"\""+this.id_vendedor.toString()+"\"":"NULL");
    }

    private String substractQuotes(String data){
        return data.substring(1, data.length()-1);
    }

    @Override
    public void deserialize(String data) {
        String[] datos = data.split(";");
        this.id_coche = Integer.parseInt(datos[0].substring(1, datos[0].length()-1));
        this.marca = this.substractQuotes(datos[1]);
        this.modelo= this.substractQuotes(datos[2]);
        this.precio= this.substractQuotes(datos[3]);
        this.kilometraje= this.substractQuotes(datos[4]);
        this.tipo= this.substractQuotes(datos[5]);
        this.estado= this.substractQuotes(datos[6]);
        if(datos[7].equals("NULL"))
            this.id_vendedor = null;
        else
            this.id_vendedor = Long.parseLong(this.substractQuotes(datos[7]));
    }
}
