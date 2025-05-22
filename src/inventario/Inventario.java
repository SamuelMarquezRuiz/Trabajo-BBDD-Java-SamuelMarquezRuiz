package inventario;

public class Inventario implements Serializer_Inventario{
    long id_inventario;
    String estado;
    Long id_coche;

    public Inventario(){
        this(0,"",null);
    }

    public Inventario(Inventario in){
        this.id_inventario=in.id_inventario;
        this.estado=in.estado;
        this.id_coche=in.id_coche;
    }

    public Inventario(String data){
        deserialize(data);
    }

    public Inventario(long id_inventario, String estado, Long id_coche){
        this.id_inventario=id_inventario;
        this.estado=estado;
        this.id_coche=id_coche;
    }

    public long getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(long id_inventario) {
        this.id_inventario = id_inventario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getId_coche() {
        return id_coche;
    }

    public void setId_coche(Long id_coche) {
        this.id_coche = id_coche;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Estado: %s, ID del coche: %d", this.id_inventario, this.estado, this.id_coche);
    }

    @Override
    public String serialize() {
        return String.format("\"%d\";\"%s\";\"%s", this.id_inventario, this.estado, this.id_coche!=null?"\""+this.id_coche.toString()+"\"":"NULL");
    }

    private String substractQuotes(String data){
        return data.substring(1, data.length()-1);
    }

    @Override
    public void deserialize(String data) {
        String[] datos = data.split(";");
        this.id_inventario = Integer.parseInt(datos[0].substring(1, datos[0].length()-1));
        this.estado = this.substractQuotes(datos[1]);        
        if(datos[2].equals("NULL"))
            this.id_coche = null;
        else
            this.id_coche = Long.parseLong(this.substractQuotes(datos[2]));
        }
}
