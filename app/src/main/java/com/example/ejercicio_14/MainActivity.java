package com.example.ejercicio_14;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Configuracion.Conexion;
import Database.Proceso;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE = 101;
    static final int PETICION_ACCESS_CAM = 201;
    String currentPhotoPath;
Button tomarfoto,ingresar,insertar,lista;
ImageView imageview;
EditText nombre,descripcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

tomarfoto= (Button) findViewById(R.id.btnfoto);
lista= (Button) findViewById(R.id.idmostrarlista);
insertar= (Button) findViewById(R.id.idinsertar);
ingresar= (Button) findViewById(R.id.guardar);
imageview = (ImageView) findViewById(R.id.imageView);
nombre = (EditText) findViewById(R.id.idnombre);
descripcion = (EditText) findViewById(R.id.iddescripcion);




tomarfoto.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        permisos();
    }
});

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeria();
            }
        });

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Agregar();

            }
        });

        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Lista.class);
                startActivity(intent);
            }
        });


    }

    private void Agregar(){

        try{
            Conexion conexion = new  Conexion (this, Proceso.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("nombres", nombre.getText().toString());
            valores.put("descripcion", descripcion.getText().toString());



            Long Resultado = db.insert(Proceso.tabladatos, "id", valores);
            Toast.makeText(this, Resultado.toString(), Toast.LENGTH_SHORT).show();
ClearScreen();
        }
        catch (Exception ex){
            Toast.makeText(this,  "error no se puedo ingresar los datos", Toast.LENGTH_LONG).show();

        }

    }
    private void ClearScreen() {
        nombre.setText(Proceso.empty);
        descripcion.setText(Proceso.empty);

    }

    private void permisos(){

        //metodo para obtener los permisos requeridos de la aplicacion
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PETICION_ACCESS_CAM);

        }else{
            //TomarFoto();
            dispatchTakePictureIntent();
        }



    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESS_CAM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
                //TomarFoto();
            }
        }else{

            Toast.makeText(getApplicationContext(), "se necestita el permiso de la camara", Toast.LENGTH_LONG).show();


        }


    }

    private void TomarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, REQUEST_IMAGE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE){
            //Bundle extra = data.getExtras();
            //Bitmap imagen = (Bitmap) extra.get("data");
            //imageView.setImageBitmap(imagen);
           try{
               File foto = new File(currentPhotoPath);
               imageview.setImageURI(Uri.fromFile(foto));
           }catch(Exception ex){
               ex.toString();
           }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.ejercicio_14.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,  photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }


    private void galeria() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }




}