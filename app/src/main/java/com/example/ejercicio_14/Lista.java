package com.example.ejercicio_14;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Configuracion.Conexion;
import Database.Datosguardar;
import Database.Proceso;

public class Lista extends AppCompatActivity {

    Conexion conexion;
    ListView listView;
    ArrayList<Datosguardar> lista;
    ArrayList<String> Arreglo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        conexion = new Conexion(this, Proceso.NameDatabase, null, 1);
        listView = (ListView)  findViewById(R.id.list_view);

        ObtenerListaPersonas();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arreglo);
        listView.setAdapter(adp);


    }

    private void ObtenerListaPersonas() {

        SQLiteDatabase db = conexion.getReadableDatabase();
        Datosguardar person = null;
        lista = new ArrayList<Datosguardar>();

        // cursor

        Cursor cursor = db.rawQuery("SELECT * FROM datos", null);

        while(cursor.moveToNext()){

            person = new Datosguardar();
            person.setId(cursor.getInt(0));
            person.setNombre(cursor.getString(1));
            person.setDescripcion(cursor.getString(2));


            lista.add(person);
        }
        cursor.close();
        FileList();


    }

    private void FileList(){
        Arreglo = new ArrayList<String>();
        for(int i = 0; i < lista.size(); i++){


            Arreglo.add(lista.get(i).getId() + " | "+
                    lista.get(i).getNombre() + " | "+
                    lista.get(i).getDescripcion()+ " | ");






        }



}
}