import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import coches.Coches;
import coches.CochesService;
import connection.ConnectionPool;
import inventario.Inventario;
import inventario.InventarioService;
import usuarios.Usuarios;
import usuarios.UsuariosService;

public class App {
    public static void listarCoche(CochesService service){
        try {
            ArrayList<Coches> coches = service.requestAll();
            if(coches.size()==0){
                System.out.println("No hay coches");
            }else{
                for (Coches c : coches) {
                    System.out.println(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listarInventario(InventarioService service){
        try {
            ArrayList<Inventario> inventarios = service.requestAll();
            if(inventarios.size()==0){
                System.out.println("No hay nada en el inventario");
            }else{
                for (Inventario i : inventarios) {
                    System.out.println(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listarUsuarios(UsuariosService service){
        try {
            ArrayList<Usuarios> usuarios = service.requestAll();
            if(usuarios.size()==0){
                System.out.println("No hay usuarios");
            }else{
                for (Usuarios u : usuarios) {
                    System.out.println(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/Proyecto-Programacion";
        String cuenta_usuario = "samuel";
        String clave = "smarrui1108";

        ConnectionPool pool = new ConnectionPool(url, cuenta_usuario, clave);
        CochesService cService = new CochesService(pool.getConnection());
        InventarioService iService = new InventarioService(pool.getConnection());
        UsuariosService uService = new UsuariosService(pool.getConnection());

        String marca, modelo, precio, kilometraje, tipo, estado, nombre, direccion, telefono, email, tipoUsuario, file;
        long id_coche = 0, id_inventario = 0, id_usuario = 0;
        Long id_vendedor;
        boolean salir = false;

        while (!salir) {
            try{
                System.out.println("1. Gestión de coches");
                System.out.println("2. Gestión de inventario");
                System.out.println("3. Gestión de usuarios");
                System.out.println("4. Salir");
                int opcion1 = Integer.parseInt(sc.nextLine());
                switch (opcion1) {
                    case 1:
                        boolean salir2 = false;
                        while (!salir2) {
                            try{
                                System.out.println("1. Listar coches");
                                System.out.println("2. Listar un coche");
                                System.out.println("3. Añadir coche");
                                System.out.println("4. Modificar coche");
                                System.out.println("5. Eliminar coche");
                                System.out.println("6. Exportar coches a CSV");
                                System.out.println("7. Importar coches desde CSV");
                                System.out.println("8. Salir");
                                int opcion2 = Integer.parseInt(sc.nextLine());
                                switch (opcion2) {
                                    case 1:
                                        listarCoche(cService);
                                        break;
                                    case 2:
                                        System.out.println("Introduce el id del coche");
                                        id_coche = Long.parseLong(sc.nextLine());
                                        Coches coche = cService.requestById(id_coche);
                                        if(coche!=null){
                                            System.out.println(coche);
                                        }else{
                                            System.out.println("No se ha encontrado el coche");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("Introduce la marca del coche");
                                        marca = sc.nextLine();
                                        System.out.println("Introduce el modelo del coche");
                                        modelo = sc.nextLine();
                                        System.out.println("Introduce el precio del coche");
                                        precio = sc.nextLine();
                                        System.out.println("Introduce el kilometraje del coche");
                                        kilometraje = sc.nextLine();
                                        System.out.println("Introduce el tipo del coche");
                                        tipo = sc.nextLine();
                                        System.out.println("Introduce el estado del coche (nuevo/usado)");
                                        estado = sc.nextLine();
                                        System.out.println("Introduce el id del vendedor");
                                        id_vendedor = Long.parseLong(sc.nextLine());
                                        try{
                                            id_coche = cService.create(new Coches(id_coche, marca, modelo, precio, kilometraje, tipo, estado, id_vendedor));
                                            System.out.println("El coche se ha añadido con el id: "+id_coche);
                                        } catch (SQLException e) {
                                            if(e.getErrorCode()==1062){
                                                System.out.println("El coche ya existe");
                                            }
                                            else{
                                                System.out.println(e);
                                            }
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Elige el coche a modificar");
                                        listarCoche(cService);
                                        System.out.println("Introduce el id del coche a modificar");
                                        id_coche = Long.parseLong(sc.nextLine());
                                        Coches cochedita = cService.requestById(id_coche);
                                        System.out.println("Introduce la marca del coche");
                                        marca = sc.nextLine();
                                        if (marca.equals("")) {
                                            marca = cochedita.getMarca();
                                        }
                                        System.out.println("Introduce el modelo del coche");
                                        modelo = sc.nextLine();
                                        if (modelo.equals("")) {
                                            modelo = cochedita.getModelo();
                                        }
                                        System.out.println("Introduce el precio del coche");
                                        precio = sc.nextLine();
                                        if (precio.equals("")) {
                                            precio = cochedita.getPrecio();
                                        }
                                        System.out.println("Introduce el kilometraje del coche");
                                        kilometraje = sc.nextLine();
                                        if (kilometraje.equals("")) {
                                            kilometraje = cochedita.getKilometraje();
                                        }
                                        System.out.println("Introduce el tipo del coche");
                                        tipo = sc.nextLine();
                                        if (tipo.equals("")) {
                                            tipo = cochedita.getTipo();
                                        }
                                        System.out.println("Introduce el estado del coche (nuevo/usado)");
                                        estado = sc.nextLine();
                                        if (estado.equals("")) {
                                            estado = cochedita.getEstado();
                                        }
                                        System.out.println("Introduce el id del vendedor");
                                        id_vendedor = Long.parseLong(sc.nextLine());
                                        if (id_vendedor==0) {
                                            id_vendedor = cochedita.getId_vendedor();
                                        }
                                        try {
                                            int rowAffected = cService.update(new Coches(id_coche, marca, modelo, precio, kilometraje, tipo, estado, id_vendedor));
                                            if(rowAffected==1)
                                                System.out.println("Coche modificado correctamente");
                                            else
                                                System.out.println("No se ha podido actualizar el coche");
                                        } catch (SQLException e) {
                                            System.out.println("No se ha podido actualizar el coche");
                                            System.out.println("Ocurrió una excepción: "+e.getMessage());
                                        }
                                        break;
                                    case 5:
                                        System.out.println("Elige el coche a eliminar");
                                        listarCoche(cService);
                                        System.out.println("Introduce el id del coche a eliminar");
                                        id_coche = Long.parseLong(sc.nextLine());
                                        try {
                                            cService.delete(id_coche);
                                        } catch (SQLException e) {
                                            System.out.println("No se ha podido eliminar el coche");
                                            System.out.println("Ocurrió una excepción: "+e.getMessage());
                                        }
                                        break;
                                    case 6:
                                        System.out.println("Escribe el archivo al que quieres exportar los datos a CSV");
                                        file = sc.nextLine();
                                        cService.exportToCSV(file);
                                        break;
                                    case 7:
                                        System.out.println("Escribe el archivo del que quieres importar los datos desde CSV");
                                        file = sc.nextLine();
                                        cService.importFromCSV(file);
                                        break;
                                    case 8:
                                        salir2 = true;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                }
                            }
                            break;
                    case 2:
                        boolean salir3 = false;
                        while (!salir3) {
                            try{
                                System.out.println("1. Listar inventario");
                                System.out.println("2. Listar un inventario");
                                System.out.println("3. Añadir inventario");
                                System.out.println("4. Modificar inventario");
                                System.out.println("5. Eliminar inventario");
                                System.out.println("6. Exportar inventario a CSV");
                                System.out.println("7. Importar inventario desde CSV");
                                System.out.println("8. Salir");
                                int opcion3 = Integer.parseInt(sc.nextLine());
                                switch (opcion3) {
                                    case 1:
                                        listarInventario(iService);
                                        break;
                                    case 2:
                                        System.out.println("Introduce el id del inventario");
                                        id_inventario = Long.parseLong(sc.nextLine());
                                        Inventario inventario = iService.requestById(id_inventario);
                                        if(inventario!=null){
                                            System.out.println(inventario);
                                        }else{
                                            System.out.println("No se ha encontrado el inventario");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("Introduce el estado del inventario");
                                        estado = sc.nextLine();
                                        System.out.println("Introduce el id del coche");
                                        id_coche = Long.parseLong(sc.nextLine());
                                        try{
                                            id_inventario = iService.create(new Inventario(id_inventario, estado, id_coche));
                                            System.out.println("El inventario se ha añadido con el id: "+id_inventario);
                                        } catch (SQLException e) {
                                            if(e.getErrorCode()==1062){
                                                System.out.println("El inventario ya existe");
                                            }
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Elige el inventario a modificar");
                                        listarInventario(iService);
                                        System.out.println("Introduce el id del inventario a modificar");
                                        id_inventario = Long.parseLong(sc.nextLine());
                                        Inventario inventarioedita = iService.requestById(id_inventario);
                                        System.out.println("Introduce el estado del inventario (disponible/vendido/reservado)");
                                        estado = sc.nextLine();
                                        if (estado.equals("")) {
                                            estado = inventarioedita.getEstado();
                                        }
                                        System.out.println("Introduce el id del coche");
                                        id_coche = Long.parseLong(sc.nextLine());
                                        if (id_coche==0) {
                                            id_coche = inventarioedita.getId_coche();
                                        }
                                        try {
                                            int rowAffected = iService.update(new Inventario(id_inventario, estado, id_coche));
                                            if(rowAffected==1)
                                                System.out.println("Inventario modificado correctamente");
                                            else
                                                System.out.println("No se ha podido actualizar el inventario");
                                        } catch (SQLException e) {
                                            System.out.println("No se ha podido actualizar el inventario");
                                            System.out.println("Ocurrió una excepción: "+e.getMessage());
                                        }
                                        break;
                                    case 5:
                                        System.out.println("Elige el inventario a eliminar");
                                        listarInventario(iService);
                                        System.out.println("Introduce el id del inventario a eliminar");
                                        id_inventario = Long.parseLong(sc.nextLine());
                                        try {
                                            iService.delete(id_inventario);
                                        } catch (SQLException e) {
                                            System.out.println("No se ha podido eliminar el inventario");
                                            System.out.println("Ocurrió una excepción: "+e.getMessage());
                                        }
                                        break;
                                    case 6:
                                        System.out.println("Escribe el archivo al que quieres exportar los datos a CSV");
                                        file = sc.nextLine();
                                        iService.exportToCSV(file);
                                        break;
                                    case 7:
                                        System.out.println("Escribe el archivo del que quieres importar los datos desde CSV");
                                        file = sc.nextLine();
                                        iService.importFromCSV(file);
                                        break;
                                    case 8:
                                        salir3 = true;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                }
                            }
                            break;
                    case 3:
                        boolean salir4 = false;
                        while (!salir4) {
                            try{
                                System.out.println("1. Listar usuarios");
                                System.out.println("2. Listar un usuario");
                                System.out.println("3. Añadir usuario");
                                System.out.println("4. Modificar usuario");
                                System.out.println("5. Eliminar usuario");
                                System.out.println("6. Exportar usuarios a CSV");
                                System.out.println("7. Importar usuarios desde CSV");
                                System.out.println("8. Salir");
                                int opcion4 = Integer.parseInt(sc.nextLine());
                                switch (opcion4) {
                                    case 1:
                                        listarUsuarios(uService);
                                        break;
                                    case 2:
                                        System.out.println("Introduce el id del usuario");
                                        id_usuario = Long.parseLong(sc.nextLine());
                                        Usuarios usuario = uService.requestById(id_usuario);
                                        if(usuario!=null){
                                            System.out.println(usuario);
                                        }else{
                                            System.out.println("No se ha encontrado el usuario");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("Introduce el nombre del usuario");
                                        nombre = sc.nextLine();
                                        System.out.println("Introduce el teléfono del usuario");
                                        telefono = sc.nextLine();
                                        System.out.println("Introduce el email del usuario");
                                        email = sc.nextLine();
                                        System.out.println("Introduce la dirección del usuario");
                                        direccion = sc.nextLine();
                                        System.out.println("Introduce el tipo del usuario (cliente/vendedor)");
                                        tipoUsuario = sc.nextLine();
                                        try{
                                            id_usuario = uService.create(new Usuarios(id_usuario, nombre, telefono, email, direccion, tipoUsuario));
                                            System.out.println("El usuario se ha añadido con el id: "+id_usuario);
                                        } catch (SQLException e) {
                                            if(e.getErrorCode()==1062){
                                                System.out.println("El usuario ya existe");
                                            }
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Elige el usuario a modificar");
                                        listarUsuarios(uService);
                                        System.out.println("Introduce el id del usuario a modificar");
                                        id_usuario = Long.parseLong(sc.nextLine());
                                        Usuarios usuarioedita = uService.requestById(id_usuario);
                                        System.out.println("Introduce el nombre del usuario");
                                        nombre = sc.nextLine();
                                        if (nombre.equals("")) {
                                            nombre = usuarioedita.getNombre();
                                        }
                                        System.out.println("Introduce la dirección del usuario");
                                        direccion = sc.nextLine();
                                        if (direccion.equals("")) {
                                            direccion = usuarioedita.getDireccion();
                                        }
                                        System.out.println("Introduce el teléfono del usuario");
                                        telefono = sc.nextLine();
                                        if (telefono.equals("")) {
                                            telefono = usuarioedita.getTelefono();
                                        }
                                        System.out.println("Introduce el email del usuario");
                                        email = sc.nextLine();
                                        if (email.equals("")) {
                                            email = usuarioedita.getEmail();
                                        }
                                        System.out.println("Introduce el tipo del usuario (cliente/vendedor)");
                                        tipoUsuario = sc.nextLine();
                                        if (tipoUsuario.equals("")) {
                                            tipoUsuario = usuarioedita.getTipo();
                                        }
                                        try {
                                            int rowAffected = uService.update(new Usuarios(id_usuario, nombre, direccion, telefono, email, tipoUsuario));
                                            if(rowAffected==1)
                                                System.out.println("Usuario modificado correctamente");
                                            else
                                                System.out.println("No se ha podido actualizar el usuario");
                                        } catch (SQLException e) {
                                            System.out.println("No se ha podido actualizar el usuario");
                                            System.out.println("Ocurrió una excepción: "+e.getMessage());
                                        }
                                        break;
                                    case 5:
                                        System.out.println("Elige el usuario a eliminar");
                                        listarUsuarios(uService);
                                        System.out.println("Introduce el id del usuario a eliminar");
                                        id_usuario = Long.parseLong(sc.nextLine());
                                        try {
                                            uService.delete(id_usuario);
                                        } catch (SQLException e) {
                                            System.out.println("No se ha podido eliminar el usuario");
                                            System.out.println("Ocurrió una excepción: "+e.getMessage());
                                        }
                                        break;
                                    case 6:
                                        System.out.println("Escribe el archivo al que quieres exportar los datos a CSV");
                                        file = sc.nextLine();
                                        uService.exportToCSV(file);
                                        break;
                                    case 7:
                                        System.out.println("Escribe el archivo del que quieres importar los datos desde CSV");
                                        file = sc.nextLine();
                                        uService.importFromCSV(file);
                                        break;
                                    case 8:
                                        salir4 = true;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                }
                            }
                            break;
                    case 4:
                        salir = true;
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sc.close();
    }
}