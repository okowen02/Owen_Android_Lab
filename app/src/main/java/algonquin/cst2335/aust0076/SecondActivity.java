package algonquin.cst2335.aust0076;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView title = findViewById(R.id.textView);
        title.setText("Welcome Back " + emailAddress);

        Button callButton = findViewById(R.id.phoneButton);
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        Button changePhoto = findViewById(R.id.pictureButton);
        ImageView profileImage = findViewById(R.id.imageView2);

        String phone = prefs.getString("PhoneNumber", "");
        phoneNumber.setText(phone);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File( getFilesDir(), "Picture.png");

        if(file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getPath());
            profileImage.setImageBitmap(theImage);
        }

        callButton.setOnClickListener(clk -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber.getText()));
            startActivity(call);
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;
                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }

                            catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });

        changePhoto.setOnClickListener(clk -> {
            cameraResult.launch(cameraIntent);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText phoneNumber = findViewById(R.id.editTextPhone);
        editor.putString("PhoneNumber", phoneNumber.getText().toString());
        editor.apply();

    }
}