package algonquin.cst2335.aust0076;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.startView);
        et = findViewById(R.id.editTextPassword);
        btn = findViewById(R.id.loginButton);

        btn.setOnClickListener( clk -> {
            String pswd = et.getText().toString();

            if(checkPasswordComplexity(pswd)) {
                tv.setText("Your password meets the requirements");
            }
            else {
                tv.setText("You shall not pass!");
            }
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