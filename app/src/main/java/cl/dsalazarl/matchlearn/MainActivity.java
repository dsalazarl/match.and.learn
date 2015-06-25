package cl.dsalazarl.matchlearn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends ActionBarActivity {

    Button topButton, bottomButton;
    TargetMatch target;
    TextView StrPuntaje;
    TextView ValPuntaje;
    TextView StrTiempo;
    TextView ValTiempo;
    TextView NombreApp;

    String saveTopText, saveBottomText, saveValPuntajeText;
    int saveTopColor, saveBottomColor, saveValPuntajeColor;
    Boolean RespuestaCorrecta;

    int score, TimePreviousTarget;
    private MediaPlayer sonidoCorrecto, sonidoIncorrecto;
    //T CounterClass timer3sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topButton = (Button) findViewById(R.id.btn_superior);
        bottomButton = (Button) findViewById(R.id.btn_inferior);

        StrPuntaje= (TextView) findViewById(R.id.text_str_puntaje);
        ValPuntaje= (TextView) findViewById(R.id.text_val_puntaje);
        StrTiempo= (TextView) findViewById(R.id.text_str_tiempo);
        ValTiempo= (TextView) findViewById(R.id.text_val_tiempo);
        NombreApp= (TextView) findViewById(R.id.text_nombre_app);

        score=0;
        TimePreviousTarget=0;
        sonidoCorrecto=MediaPlayer.create(MainActivity.this, R.raw.sonido_correcto);
        sonidoIncorrecto=MediaPlayer.create(this, R.raw.sonido_incorrecto);

        //obtener nueva targeta
        target = new TargetMatch();
        //parametros para metodos onSaveInstanceState() y onRestoreInstanceState()
        saveTopColor=target.color1;
        saveBottomColor=target.color2;
        RespuestaCorrecta=target.correct;
        //propiedades definidas
        topButton.setText(target.question);
        bottomButton.setText(target.answer);
        topButton.setBackgroundColor(target.color1);
        bottomButton.setBackgroundColor(target.color2);

        ValPuntaje.setText(" "+score+" pts.");
        ValTiempo.setText(" 1 minuto");
        //T timer3sec = new CounterClass(3000, 1000);//1 minuto y cuanto avanza (en milisegundos) 59999
        //fuentes
        Typeface HelveticaNormal= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLight.otf");
        Typeface HelveticaItalica= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLightItal.otf");
        StrPuntaje.setTypeface(HelveticaNormal);
        ValPuntaje.setTypeface(HelveticaNormal);
        StrTiempo.setTypeface(HelveticaNormal);
        ValTiempo.setTypeface(HelveticaNormal);
        NombreApp.setTypeface(HelveticaItalica);
    }

    //lo siguiente es para el DialogAlert que diga instrucciones y boton Entendido, que el boton ejecute el timer.start
    //topButton.setOnClickListener(new View.OnClickListener() {
    //
    //    @Override
    //    public void onClick(View v) {
    //        timer.start();
    //    }
    //});

    public void targetCheck (View view){
        if(RespuestaCorrecta==true){
            sonidoCorrecto.start();
            score+=5;
            ValPuntaje.setTextColor(Color.parseColor("#FF00A20E"));
        }
        else if(RespuestaCorrecta==false){
            sonidoIncorrecto.start();
            score-=3;
            ValPuntaje.setTextColor(Color.RED);
        }
        ValPuntaje.setText(" "+score+" pts.");
        //renovar tarjeta y aquellos que usan sus atributos
        target= new TargetMatch();
        topButton.setText(target.question);
        bottomButton.setText(target.answer);
        topButton.setBackgroundColor(target.color1);
        bottomButton.setBackgroundColor(target.color2);
        saveTopColor=target.color1;
        saveBottomColor=target.color2;
        RespuestaCorrecta=target.correct;
        //T timer3sec.start();
    }

    //para que al cambiar la orientacion del dispositivo, los valores no se reseteen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //obtener valores a guardar
        saveTopText= topButton.getText().toString();
        saveBottomText=bottomButton.getText().toString();
        saveValPuntajeText=ValPuntaje.getText().toString();
        saveValPuntajeColor=ValPuntaje.getCurrentTextColor();
        //guardar valores
        outState.putString("pregunta", saveTopText);
        outState.putString("respuesta", saveBottomText);
        outState.putInt("colorido1", saveTopColor);
        outState.putInt("colorido2", saveBottomColor);
        outState.putString("txt_puntaje", saveValPuntajeText);
        outState.putInt("color_puntaje", saveValPuntajeColor);
        outState.putInt("score", score);
        outState.putBoolean("correcto", RespuestaCorrecta);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //texto
        topButton.setText(savedInstanceState.getString("pregunta"));
        bottomButton.setText(savedInstanceState.getString("respuesta"));
        ValPuntaje.setText(savedInstanceState.getString("txt_puntaje"));
        //colores
        saveTopColor=savedInstanceState.getInt("colorido1");
        saveBottomColor=savedInstanceState.getInt("colorido2");
        topButton.setBackgroundColor(saveTopColor);
        bottomButton.setBackgroundColor(saveBottomColor);
        ValPuntaje.setTextColor(savedInstanceState.getInt("color_puntaje"));
        //puntaje
        score=savedInstanceState.getInt("score");
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


    //Inclusión de tiempo. Actualmente lo que hace es contar 3 segundos y luego verifica las tarjetas y las renueva
    //Necesitamos poner 2 tiempos, el visible que solo cuente 1 minutos atrás hasta que termine el juego y muestre en
    //un nuevo DialogAlert el puntaje final obtenido y opción para comenzar denuevo. El segundo tiempo debe hacer lo que
    //hace esta clase CounterClass como la tengo definida. De hecho podría hacerse con un solo tiempo, con una variable
    //que guarde el segundo en que se respondió la última tarjeta y lo compare con el tiempo actual, si esa diferencia
    //es igual a 3000 (3segundos) verifique las tarjetas. Me he complicado harto con lo de los tiempos, ojalá alguien
    //pueda hacerlo.
    //quiten los "//" a lo que sigue para ver qué es lo que hace el CounterClass que alcancé a definir y recordar quitarle
    //los "//" a las partes que lo necesitan en el resto del código. lo sabrán porque viene así: "//T " y luego el cogido
    //la T es por tiempo.
    //public class CounterClass extends CountDownTimer {
    //
    //    public CounterClass(long millisInFuture, long countDownInterval) {
    //        super(millisInFuture, countDownInterval);
    //    }
    //
    //    @Override
    //    public void onTick(long millisUntilFinished) {
    //        String Seg = String.format("%02d",
    //                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
    //        System.out.println(Seg);
    //        ValTiempo.setText(" "+Seg+" seg.");
    //
    //    }
    //
    //    @Override
    //    public void onFinish() {
    //        if(RespuestaCorrecta==true){
    //            sonidoCorrecto.start();
    //            score+=5;
    //            ValPuntaje.setTextColor(Color.parseColor("#FF00A20E"));
    //        }
    //        else if(RespuestaCorrecta==false){
    //            sonidoIncorrecto.start();
    //            score-=3;
    //            ValPuntaje.setTextColor(Color.RED);
    //        }
    //        ValPuntaje.setText(" "+score+" pts.");
    //        //renovar tarjeta y aquellos que usan sus atributos
    //        target= new TargetMatch();
    //        topButton.setText(target.question);
    //        bottomButton.setText(target.answer);
    //        topButton.setBackgroundColor(target.color1);
    //        bottomButton.setBackgroundColor(target.color2);
    //        saveTopColor=target.color1;
    //        saveBottomColor=target.color2;
    //        RespuestaCorrecta=target.correct;
    //        timer3sec.start();
    //    }
    //}


    //detector de double tap. sigle tap para correcto y double tap para incorrecto
    //int i = 0;
    //btn.setOnClickListener(new OnClickListener() {
    //
    //    @Override
    //    public void onClick(View v) {
    //        // TODO Auto-generated method stub
    //        i++;
    //        Handler handler = new Handler();
    //        Runnable r = new Runnable() {
    //
    //            @Override
    //            public void run() {
    //                i = 0;
    //            }
    //        };
    //
    //        if (i == 1) {
    //            //Single click
    //            handler.postDelayed(r, 250);
    //        } else if (i == 2) {
    //            //Double click
    //            i = 0;
    //            ShowDailog();
    //        }
    //
    //
    //    }
    //});

    //DialogAlert para pausa/confirmar salida del juego. Para mejorarlo, usar DialogFragment
    @Override
    public void onBackPressed() {
        //T timer3sec.cancel();
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Juego Pausado")
                .setMessage("¿Seguro que quieres salir de este asombroso juego?")
                .setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //T timer3sec.start();
                    }
                })
                .show();
    }
}
