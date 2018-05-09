package com.example.lattitude.w2d2e1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String picturePath="";
    File destination;
    final String TAG = "TAG_ACTIVITY_MAIN";
    static final String RESULT_MESSAGE = "com.example.lattitude.w2d2e1";
    static final int CAM_REQUEST = 6666;
    EditText name,lastname,email,search;
    TextView photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        photoPath = findViewById(R.id.textView);
        if( requestCode == CAM_REQUEST && resultCode == Activity.RESULT_OK ) {
            //Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            photoPath.setText(picturePath);
        }else{
            photoPath.setText("take photo");

        }
    }

    public void saveUser(View v){
        name = findViewById(R.id.nameTxt);
        lastname = findViewById(R.id.lastNameTxt);
        email =findViewById(R.id.emailTxt);

        usersDB usersdb = new usersDB(this, "usersdb", null, 1);
        SQLiteDatabase db = usersdb.getWritableDatabase();

        if(db!=null){
            String sqlInsert = "INSERT INTO "+usersdb.getUSERS_TABLE_NAME()+
                    "("+usersdb.getCOLUMN_2()+","+usersdb.getCOLUMN_3()+","+usersdb.getCOLUMN_4()+","+usersdb.getCOLUMN_5()+") " +
                    "VALUES('"+name.getText().toString()+
                    "','"+lastname.getText().toString()+
                    "','"+email.getText().toString()+
                    "','"+picturePath+"')";
            Log.d(TAG,sqlInsert);
            try{
                db.execSQL(sqlInsert);
                db.close();
                name.setText("");
                lastname.setText("");
                email.setText("");
            }catch(Exception e){
                Log.d(TAG,e.toString());
                Toast.makeText(this,"DB Error!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"DB has not been opened",Toast.LENGTH_SHORT).show();
        }
    }

    public void findUserByName(View v){
        search = findViewById(R.id.searchTxt);
        Cursor cursor = null;
        int foundUser = 0;

        usersDB usersdb = new usersDB(this, "usersdb", null, 1);
        SQLiteDatabase db = usersdb.getWritableDatabase();

        if(search.getText().toString()!=null || search.getText().toString()!=""){
            try{
                String sqlSelect = "SELECT * FROM "+usersdb.getUSERS_TABLE_NAME()+" WHERE "+usersdb.getCOLUMN_2()+"='"+search.getText().toString()+"'";
                String tmpLastName="",tmpName="",tmpEmail="",tmpImagePath="";
                String[] userInfo = new String[5];
                cursor = db.rawQuery(sqlSelect,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    tmpLastName=cursor.getString(cursor.getColumnIndex(usersdb.getCOLUMN_2()));
                    tmpName=cursor.getString(cursor.getColumnIndex(usersdb.getCOLUMN_3()));
                    tmpEmail=cursor.getString(cursor.getColumnIndex(usersdb.getCOLUMN_4()));
                    tmpImagePath=cursor.getString(cursor.getColumnIndex(usersdb.getCOLUMN_5()));
                    if(tmpLastName.contentEquals(search.getText().toString())){
                        foundUser = cursor.getInt(cursor.getColumnIndex(usersdb.getCOLUMN_1()));
                        break;
                    }
                    cursor.moveToNext();
                }
                if(foundUser>0){
                    userInfo[0] = Integer.toString(foundUser);
                    userInfo[1] = tmpName;
                    userInfo[2] = tmpLastName;
                    userInfo[3] = tmpEmail;
                    userInfo[4] = tmpImagePath;
                    Intent lauchDetailsActivities = new Intent(this,Details.class);
                    lauchDetailsActivities.putExtra(RESULT_MESSAGE, userInfo);
                    startActivity(lauchDetailsActivities);
                }else{
                    Toast.makeText(this,"contact was not found",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                db.close();
            }catch(Exception e){
                Log.d(TAG,e.toString());
                Toast.makeText(this,"DB Error!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"search field cannot be blank",Toast.LENGTH_SHORT).show();
        }

    }

    public void choosePicture(View v){
        requestCameraPermissions();
        try {
            Intent lauchCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureName = getPictureName();
            File imageFile = new File(pictureDirectory,pictureName);
            Uri pictureUri = (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N)?Uri.parse(pictureDirectory+"/"+pictureName):Uri.fromFile(imageFile);
            picturePath = pictureDirectory+"/"+pictureName;
            lauchCamera.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
            startActivityForResult(lauchCamera,CAM_REQUEST);

        }catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "w2d2e1"+timestamp+".jpg";
    }

    public void requestCameraPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }
    }
}
