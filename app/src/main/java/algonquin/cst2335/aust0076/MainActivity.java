package algonquin.cst2335.aust0076;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.aust0076.databinding.ActivityMainBinding;

/**
 * This page asks the user for a password, and checks if the password meets the complexity requirements
 *
 * @author Owen Austin
 * @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity {

    /** This contains the text view in the middle of the screen */
    private TextView tv;

    /** This contains the password input field */
    private EditText et;

    /** This contains the login button */
    private Button btn;
    protected String cityName;
    RequestQueue queue = null;
//    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(this);

        binding.loginButton.setOnClickListener(click -> {
            cityName = binding.editTextPassword.getText().toString();
            String encodedName = "ottawa";
            try {
                encodedName = URLEncoder.encode(cityName, "UTF-8");
            } catch(UnsupportedEncodingException e) {e.printStackTrace();}

            String api = "bcfda8a3a002b1493577510320a9a62c";
            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + encodedName
                    + "&appid=" + api + "&units=metric";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {

                        try {
                            JSONArray weatherArray = response.getJSONArray ( "weather" );
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");

                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");


                            String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";



                                ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        // Do something with loaded bitmap...
                                        binding.icon.setImageBitmap(bitmap);
//                                        image = bitmap;
                                        Bitmap image = bitmap;
                                        try {
                                            image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        int i = 0;

                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {
                                        int i = 0;
                                });


                            queue.add(imgReq);

                            runOnUiThread(() -> {
                                binding.temp.setText("Current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);

                                binding.min.setText("Min temperature is " + min);
                                binding.min.setVisibility(View.VISIBLE);

                                binding.max.setText("Max temperature is " + max);
                                binding.max.setVisibility(View.VISIBLE);

                                binding.humidity.setText("Humidity is " + humidity);
                                binding.humidity.setVisibility(View.VISIBLE);

                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);

//                                binding.icon.setImageBitmap();
                                binding.icon.setVisibility(View.VISIBLE);
                            });

                        }catch(JSONException e) {
                            e.printStackTrace();
                        }

                    } ,
                    (error) -> {int i = 0;} );
            queue.add(request);
        });
    }

    /**
     * This function checks the complexity of the password entered by the user. Checks for an
     *      uppercase, lowercase, number and special character
     *
     * @param pswd Password string being checked
     * @return returns whether or not the password is complex enough
     */
    boolean checkPasswordComplexity(String pswd) {

        boolean upper, lower, number, special;
        upper = lower = number = special = false;
        char c;

        for(int i = 0; i < pswd.length(); i++) {

            c = pswd.charAt(i);

            if(Character.isUpperCase(c)) {
                upper = true;
            }
            else if(Character.isLowerCase(c)) {
                lower = true;
            }
            else if(Character.isDigit(c)) {
                number = true;
            }
            else if(isSpecialCharacter(c)) {
                special = true;
            }

        }

        if(!upper) {
            Toast.makeText(this, "Password does not contain an upper case character", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!lower) {
            Toast.makeText(this, "Password does not contain a lower case character", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!number) {
            Toast.makeText(this, "Password does not contain a number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!special) {
            Toast.makeText(this, "Password does not contain a special character", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }

    }

    /**
     * This function checks if a character is one of #$%^&*!@?
     *
     * @param c the character being checked
     * @return returns whether or not the character is one of #$%^&*!@? or not
     */
    boolean isSpecialCharacter(char c) {

        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }

    }

}