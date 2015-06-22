package cl.dsalazarl.matchlearn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends ActionBarActivity {

    Button topButton, bottomButton;
    TargetMatch target;
    TextView StrPuntaje;
    TextView ValPuntaje;
    TextView StrTiempo;
    TextView ValTiempo;
    TextView NombreApp;

    String saveTopText, saveBottomText;
    int saveTopColor, saveBottomColor;
    Boolean RespuestaCorrecta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topButton = (Button) findViewById(R.id.btn_superior);
        bottomButton = (Button) findViewById(R.id.btn_inferior);
        //obtener nueva targeta
        target = new TargetMatch();
        //parametros para metodos onSaveInstanceState() y onRestoreInstanceState()
        saveTopColor=target.color1;
        saveBottomColor=target.color2;
        RespuestaCorrecta=target.correct;
        //propiedades de botones
        topButton.setText(target.question);
        bottomButton.setText(target.answer);
        topButton.setBackgroundColor(target.color1);
        bottomButton.setBackgroundColor(target.color2);
        //fuentes
        StrPuntaje= (TextView) findViewById(R.id.text_str_puntaje);
        ValPuntaje= (TextView) findViewById(R.id.text_val_puntaje);
        StrTiempo= (TextView) findViewById(R.id.text_str_tiempo);
        ValTiempo= (TextView) findViewById(R.id.text_val_tiempo);
        NombreApp= (TextView) findViewById(R.id.text_nombre_app);
        Typeface HelveticaNormal= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLight.otf");
        Typeface HelveticaItalica= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLightItal.otf");
        StrPuntaje.setTypeface(HelveticaNormal);
        ValPuntaje.setTypeface(HelveticaNormal);
        StrTiempo.setTypeface(HelveticaNormal);
        ValTiempo.setTypeface(HelveticaNormal);
        NombreApp.setTypeface(HelveticaItalica);
    }
    //para que al cambiar la orientacion del dispositivo, los valores no se reseteen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //obtener Strings a guardar
        saveTopText= topButton.getText().toString();
        saveBottomText=bottomButton.getText().toString();
        //guardar valores
        outState.putString("pregunta", saveTopText);
        outState.putString("respuesta", saveBottomText);
        outState.putInt("colorido1", saveTopColor);
        outState.putInt("colorido2", saveBottomColor);
        outState.putBoolean("correcto", RespuestaCorrecta);        //falta definir que hacer con target.correct
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //texto
        topButton.setText(savedInstanceState.getString("pregunta"));
        bottomButton.setText(savedInstanceState.getString("respuesta"));
        //colores
        saveTopColor=savedInstanceState.getInt("colorido1");
        saveBottomColor=savedInstanceState.getInt("colorido2");
        topButton.setBackgroundColor(saveTopColor);
        bottomButton.setBackgroundColor(saveBottomColor);
        //falta definir que hacer con target.correct
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TargetMatch {

        private boolean correct;
        private String question,answer;
        private int color1, color2;

        public TargetMatch (){

            color1 = generateRandomColor(false);
            color2 = generateRandomColor(false);
            int divisor = (int)(Math.random()*12+1);
            int dividendo = (int)(Math.random()*20+divisor);
            this.question = dividendo+"\u00F7"+divisor; // "\u00F7" es el simbolo clasico de division (Unicode value)
            int cuociente, resto;
            double random = Math.random();
            if(random<=0.8 && random >=0.2){
                cuociente = dividendo/divisor;
                resto = dividendo-divisor*cuociente;
            }
            else if(random > 0.8){
                cuociente = dividendo/(divisor+(int)(Math.random()*(dividendo-divisor-1)+1));
                resto = (int) (Math.random()*(divisor-1));
            }
            else{
                cuociente = dividendo/(divisor-(int)(Math.random()*divisor-1));
                resto = (int) (Math.random()*(divisor-1));
            }
            if(dividendo==divisor*cuociente+resto){
                this.correct=true;
            }
            else{
                this.correct=false;
            }
            if(resto != 0) {
                this.answer = cuociente + " con resto " + resto;
            }
            else{
                this.answer = cuociente + "";
            }

        }

        private int generateRandomColor(boolean itsDark) {
            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            int col;
            if (!itsDark){
                col=255;
                red = (int)((red + col) / 2);
                green = (int)((green + col) / 2);
                blue = (int)((blue + col) / 2);
            }
            int color = android.graphics.Color.rgb(red,green,blue);
            return color;
        }

    }
    //DialogAlert para confirmar salida del juego. Para mejorarlo, usar DialogFragment
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Salida")
                .setMessage("¿Seguro que quieres salir de este asombroso juego?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
