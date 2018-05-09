package com.example.lattitude.w2d2e1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {
    TextView name,lastname,email;
    ImageView contactPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String[] userInfo = intent.getStringArrayExtra(MainActivity.RESULT_MESSAGE);

        name = findViewById(R.id.nameTxtView);
        lastname = findViewById(R.id.lastNameTxtView);
        email = findViewById(R.id.emailTxtView);
        contactPicture = findViewById(R.id.imageView);

        name.setText(userInfo[1]);
        lastname.setText(userInfo[2]);
        email.setText(userInfo[3]);

        Toast.makeText(this,userInfo[4],Toast.LENGTH_SHORT).show();
        if(userInfo[4].length()>0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(userInfo[4], options);
            contactPicture.setImageBitmap(bitmap);
        }
    }



}
