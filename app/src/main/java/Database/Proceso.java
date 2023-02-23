package Database;

public class Proceso {


    public static final String NameDatabase = "insertar";


    public static final String tabladatos = "datos";


    public static final String CreateTBdatos =
            "CREATE TABLE datos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombres TEXT," +
                    "descripcion TEXT )";

    public static final String DropTabledatos = "DROP TABLE IF EXISTS datos";


    public static final String empty = "";
}
