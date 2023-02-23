package Configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Database.Proceso;

public class Conexion extends SQLiteOpenHelper {

    public Conexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){

        super(context, dbname, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Proceso.CreateTBdatos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Proceso.DropTabledatos);
    }
}
