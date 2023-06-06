package algonquin.cst2335.aust0076;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView title = findViewById(R.id.textView);
        title.setText("Welcome Back " + emailAddress);

        Button callButton = findViewById(R.id.phoneButton);
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        Button changePhoto = findViewById(R.id.pictureButton);
        ImageView profileImage = findViewById(R.id.imageView2);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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
                        }
                    }
                });

        changePhoto.setOnClickListener(clk -> {
            cameraResult.launch(cameraIntent);
        });

    }
}