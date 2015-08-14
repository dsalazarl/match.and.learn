package cl.dsalazarl.matchlearn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.TimeUnit;


//public class MainActivity extends ActionBarActivity {
public class MainActivity extends Activity{
    Button topButton, bottomButton, correctButton, incorrectButton;
    TargetMatch target;
    TextView StrPuntaje;
    TextView ValPuntaje;
    TextView StrTiempo;
    TextView ValTiempo;
    TextView NombreApp;

    String saveTopText, saveBottomText, saveValPuntajeText, saveValTiempo;
    int saveTopColor, saveBottomColor, saveValPuntajeColor;
    Boolean RespuestaCorrecta;

    int score;
    private MediaPlayer sonidoCorrecto, sonidoIncorrecto, sonidoFinal;
    CountDownTimerWithPause timer;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //en .xml bajo la primera linea?
        //tools:context=".MainActivity"
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        topButton = (Button) findViewById(R.id.btn_superior);
        bottomButton = (Button) findViewById(R.id.btn_inferior);
        correctButton= (Button) findViewById(R.id.btn_correct);
        incorrectButton= (Button) findViewById(R.id.btn_incorrect);

        StrPuntaje= (TextView) findViewById(R.id.text_str_puntaje);
        ValPuntaje= (TextView) findViewById(R.id.text_val_puntaje);
        StrTiempo= (TextView) findViewById(R.id.text_str_tiempo);
        ValTiempo= (TextView) findViewById(R.id.text_val_tiempo);
        NombreApp= (TextView) findViewById(R.id.text_nombre_app);

        score=0;
        sonidoCorrecto=MediaPlayer.create(MainActivity.this, R.raw.sonido_correcto);
        sonidoIncorrecto=MediaPlayer.create(this, R.raw.sonido_incorrecto);
        sonidoFinal=MediaPlayer.create(this, R.raw.sonido_final);

        //crear nueva targeta inicial
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

        ValPuntaje.setText(" "+score);
        ValTiempo.setText(" 1 minuto");
        timer = new CountDownTimerWithPause(59999, 1000, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                String Seg = String.format("%02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                System.out.println(Seg);
                ValTiempo.setText(" "+Seg+" seg");
            }

            @Override
            public void onFinish() {
                topButton.setVisibility(View.INVISIBLE);
                bottomButton.setVisibility(View.INVISIBLE);
                incorrectButton.setVisibility(View.INVISIBLE);
                correctButton.setVisibility(View.INVISIBLE);
                sonidoFinal.start();
                ValTiempo.setText("- -");
                ValTiempo.setTextColor(Color.RED);
                SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                int oldScore = prefs.getInt("maxscore", 0);
                if(score > oldScore ){
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt("maxscore", score);
                    edit.commit();
                }
                new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Juego Terminado")
                        .setMessage("¡Felicitaciones! Lograste un puntaje de "+score+" puntos y el maximo fue "+prefs.getInt("maxscore",0) +". ¿Deseas volver a jugar?")
                        .setCancelable(false)
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                timer.cancel();
                                MainActivity.this.finish();
                            }
                        })
                        .setPositiveButton("Comenzar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                score = 0;
                                ValPuntaje.setText(" "+score);
                                ValPuntaje.setTextColor(Color.parseColor("#ff000000"));
                                ValTiempo.setTextColor(Color.parseColor("#ff000000"));
                                topButton.setVisibility(View.VISIBLE);
                                bottomButton.setVisibility(View.VISIBLE);
                                incorrectButton.setVisibility(View.VISIBLE);
                                correctButton.setVisibility(View.VISIBLE);
                                //renovar tarjeta y aquellos que usan sus atributos
                                target= new TargetMatch();
                                topButton.setText(target.question);
                                bottomButton.setText(target.answer);
                                topButton.setBackgroundColor(target.color1);
                                bottomButton.setBackgroundColor(target.color2);
                                saveTopColor=target.color1;
                                saveBottomColor=target.color2;
                                RespuestaCorrecta=target.correct;
                                timer.create();
                            }
                        })
                        .show();
            }
        };

        //fuentes
        Typeface HelveticaNormal= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLight.otf");
        Typeface HelveticaItalica= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLightItal.otf");
        StrPuntaje.setTypeface(HelveticaNormal);
        ValPuntaje.setTypeface(HelveticaNormal);
        StrTiempo.setTypeface(HelveticaNormal);
        ValTiempo.setTypeface(HelveticaNormal);
        NombreApp.setTypeface(HelveticaItalica);
        System.out.print("hola");
        //inicio visual para el usuario. El problema es que al cambiar orientacion mientras se juega,
        // esto comienza denuevo (incluso sin terminar el conteo y lo que eso conlleva.
        //new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        //        .setIcon(android.R.drawable.ic_dialog_alert)
        //        .setTitle("Instrucciones")
        //        .setMessage("Presione el boton verde para indicar un match correcto o el boton rojo para indicar un match incorrecto")
        //        .setCancelable(false)
        //        .setNegativeButton("Comenzar", new DialogInterface.OnClickListener() {
        //            @Override
        //            public void onClick(DialogInterface dialog, int which) {

                        timer.create();
        //            }
        //        })
        //        .show();
    }

    public void targetCheckR (View view){
        if(RespuestaCorrecta==true){
            sonidoCorrecto.start();
            score+=5;
            ValPuntaje.setTextColor(Color.parseColor("#FF00A20E"));
        }
        else if(RespuestaCorrecta==false){
            sonidoIncorrecto.start();
            score-=5;
            ValPuntaje.setTextColor(Color.RED);
        }
        ValPuntaje.setText(" "+score);
        //renovar tarjeta y aquellos que usan sus atributos
        target= new TargetMatch();
        topButton.setText(target.question);
        bottomButton.setText(target.answer);
        topButton.setBackgroundColor(target.color1);
        bottomButton.setBackgroundColor(target.color2);
        saveTopColor=target.color1;
        saveBottomColor=target.color2;
        RespuestaCorrecta=target.correct;
    }
    public void targetCheckL (View view){
        if(RespuestaCorrecta==false){
            sonidoCorrecto.start();
            score+=5;
            ValPuntaje.setTextColor(Color.parseColor("#FF00A20E"));
        }
        else if(RespuestaCorrecta==true){
            sonidoIncorrecto.start();
            score-=5;
            ValPuntaje.setTextColor(Color.RED);
        }
        ValPuntaje.setText(" "+score);
        //renovar tarjeta y aquellos que usan sus atributos
        target= new TargetMatch();
        topButton.setText(target.question);
        bottomButton.setText(target.answer);
        topButton.setBackgroundColor(target.color1);
        bottomButton.setBackgroundColor(target.color2);
        saveTopColor=target.color1;
        saveBottomColor=target.color2;
        RespuestaCorrecta=target.correct;
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
        saveValTiempo = ValTiempo.getText().toString();
        //guardar valores
        outState.putString("pregunta", saveTopText);
        outState.putString("respuesta", saveBottomText);
        outState.putInt("colorido1", saveTopColor);
        outState.putInt("colorido2", saveBottomColor);
        outState.putString("txt_puntaje", saveValPuntajeText);
        outState.putInt("color_puntaje", saveValPuntajeColor);
        outState.putInt("score", score);
        outState.putString("time", saveValTiempo);
        outState.putBoolean("correcto", RespuestaCorrecta);
        timer.cancel();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        timer.cancel();
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
        int tiempo = Integer.parseInt(savedInstanceState.getString("time").substring(1,3))*1000;
        timer = new CountDownTimerWithPause(tiempo, 1000, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                String Seg = String.format("%02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                System.out.println(Seg);
                ValTiempo.setText(" "+Seg+" seg");
            }

            @Override
            public void onFinish() {
                topButton.setVisibility(View.INVISIBLE);
                bottomButton.setVisibility(View.INVISIBLE);
                incorrectButton.setVisibility(View.INVISIBLE);
                correctButton.setVisibility(View.INVISIBLE);
                sonidoFinal.start();
                ValTiempo.setText("- -");
                ValTiempo.setTextColor(Color.RED);
                SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                int oldScore = prefs.getInt("maxscore", 0);
                if(score > oldScore ){
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt("maxscore", score);
                    edit.commit();
                }
                new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Juego Terminado")
                        .setMessage("¡Felicitaciones! Lograste un puntaje de "+score+" puntos y el maximo fue "+prefs.getInt("maxscore",0) +". ¿Deseas volver a jugar?")
                        .setCancelable(false)
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                timer.cancel();
                                MainActivity.this.finish();
                            }
                        })
                        .setPositiveButton("Comenzar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                score = 0;
                                ValPuntaje.setText(" "+score);
                                ValPuntaje.setTextColor(Color.parseColor("#ff000000"));
                                ValTiempo.setTextColor(Color.parseColor("#ff000000"));
                                topButton.setVisibility(View.VISIBLE);
                                bottomButton.setVisibility(View.VISIBLE);
                                incorrectButton.setVisibility(View.VISIBLE);
                                correctButton.setVisibility(View.VISIBLE);
                                //renovar tarjeta y aquellos que usan sus atributos
                                target= new TargetMatch();
                                topButton.setText(target.question);
                                bottomButton.setText(target.answer);
                                topButton.setBackgroundColor(target.color1);
                                bottomButton.setBackgroundColor(target.color2);
                                saveTopColor=target.color1;
                                saveBottomColor=target.color2;
                                RespuestaCorrecta=target.correct;
                                timer = new CountDownTimerWithPause(59999, 1000, true) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        String Seg = String.format("%02d",
                                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                                        System.out.println(Seg);
                                        ValTiempo.setText(" "+Seg+" seg");
                                    }

                                    @Override
                                    public void onFinish() {
                                        topButton.setVisibility(View.INVISIBLE);
                                        bottomButton.setVisibility(View.INVISIBLE);
                                        incorrectButton.setVisibility(View.INVISIBLE);
                                        correctButton.setVisibility(View.INVISIBLE);
                                        sonidoFinal.start();
                                        ValTiempo.setText("- -");
                                        ValTiempo.setTextColor(Color.RED);
                                        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                                        int oldScore = prefs.getInt("maxscore", 0);
                                        if(score > oldScore ){
                                            SharedPreferences.Editor edit = prefs.edit();
                                            edit.putInt("maxscore", score);
                                            edit.commit();
                                        }
                                        new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Juego Terminado")
                                                .setMessage("¡Felicitaciones! Lograste un puntaje de "+score+" puntos y el maximo fue "+prefs.getInt("maxscore",0) +". ¿Deseas volver a jugar?")
                                                .setCancelable(false)
                                                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        timer.cancel();
                                                        MainActivity.this.finish();
                                                    }
                                                })
                                                .setPositiveButton("Comenzar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        score = 0;
                                                        ValPuntaje.setText(" "+score);
                                                        ValPuntaje.setTextColor(Color.parseColor("#ff000000"));
                                                        ValTiempo.setTextColor(Color.parseColor("#ff000000"));
                                                        topButton.setVisibility(View.VISIBLE);
                                                        bottomButton.setVisibility(View.VISIBLE);
                                                        incorrectButton.setVisibility(View.VISIBLE);
                                                        correctButton.setVisibility(View.VISIBLE);
                                                        //renovar tarjeta y aquellos que usan sus atributos
                                                        target= new TargetMatch();
                                                        topButton.setText(target.question);
                                                        bottomButton.setText(target.answer);
                                                        topButton.setBackgroundColor(target.color1);
                                                        bottomButton.setBackgroundColor(target.color2);
                                                        saveTopColor=target.color1;
                                                        saveBottomColor=target.color2;
                                                        RespuestaCorrecta=target.correct;

                                                        timer.create();
                                                    }
                                                })
                                                .show();
                                    }
                                };
                                timer.create();
                            }
                        })
                        .show();
            }
        };
        timer.create();

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

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String Seg = String.format("%02d",
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            System.out.println(Seg);
            ValTiempo.setText(" "+Seg+" seg");
        }

        @Override
        public void onFinish() {
            topButton.setVisibility(View.INVISIBLE);
            bottomButton.setVisibility(View.INVISIBLE);
            incorrectButton.setVisibility(View.INVISIBLE);
            correctButton.setVisibility(View.INVISIBLE);
            sonidoFinal.start();
            ValTiempo.setText("- -");
            ValTiempo.setTextColor(Color.RED);
            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            int oldScore = prefs.getInt("maxscore", 0);
            if(score > oldScore ){
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("maxscore", score);
                edit.commit();
            }
            new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Juego Terminado")
                    .setMessage("¡Felicitaciones! Lograste un puntaje de "+score+" puntos y el maximo fue "+prefs.getInt("maxscore",0) +". ¿Deseas volver a jugar?")
                    .setCancelable(false)
                    .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            timer.cancel();
                            MainActivity.this.finish();
                        }
                    })
                    .setPositiveButton("Comenzar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            score = 0;
                            ValPuntaje.setText(" "+score+" pts.");
                            ValPuntaje.setTextColor(Color.parseColor("#ff000000"));
                            ValTiempo.setTextColor(Color.parseColor("#ff000000"));
                            topButton.setVisibility(View.VISIBLE);
                            bottomButton.setVisibility(View.VISIBLE);
                            incorrectButton.setVisibility(View.VISIBLE);
                            correctButton.setVisibility(View.VISIBLE);
                            //renovar tarjeta y aquellos que usan sus atributos
                            target= new TargetMatch();
                            topButton.setText(target.question);
                            bottomButton.setText(target.answer);
                            topButton.setBackgroundColor(target.color1);
                            bottomButton.setBackgroundColor(target.color2);
                            saveTopColor=target.color1;
                            saveBottomColor=target.color2;
                            RespuestaCorrecta=target.correct;
                            timer.resume();
                            }
                    })
                    .show();
        }
    }

    public String consejo(){
        Random random = new Random();
        int nConsejo = random.nextInt(10);
        String nuestroConsejo = "";

        switch (nConsejo) {
            case 1:
                nuestroConsejo = "Tener la conciencia limpia es síntoma de mala memoria.";
                break;
            case 2:
                nuestroConsejo = " ¿Por qué temblará la gelatina? ¿Será que sabe lo que le espera?";
                break;
            case 4:
                nuestroConsejo = "El mejor amigo del perro es otro perro.";
                break;
            case 5:
                nuestroConsejo = "El fabricante de ventiladores vive del aire.";
                break;
            case 6:
                nuestroConsejo = "Los mosquitos mueren entre aplausos.";
                break;
            case 7:
                nuestroConsejo = "Arreglar los problemas económicos es fácil, lo único que se necesita es dinero.";
                break;
            case 8:
                nuestroConsejo = "El eco siempre dice la última palabra.";
                break;
            case 9:
                nuestroConsejo = "¿Cuál es el animal que después de muerto da muchas vueltas? El pollo asado.";
                break;
            case 10:
                nuestroConsejo = "Lo importante no es ganar, sino hacer perder al otro.";
                break;
            default:
                nuestroConsejo = "El diabético no puede ir de luna de miel.";
                break;
        }

        return nuestroConsejo;
    }
    final Class<MainActiviti> mein = MainActiviti.class;
    //DialogAlert para pausa/confirmar salida del juego. Para mejorarlo, usar DialogFragment
    @Override
    public void onBackPressed() {
        timer.pause();


        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setIcon(android.R.drawable.ic_media_pause)
                .setTitle("Juego Pausado")
                .setMessage("Recuerda: " + consejo() + "\n\n¿Realmente deseas salir?")
                .setCancelable(false)
                .setPositiveButton("Sí, pero volveré", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        timer.cancel();

                        MainActivity.this.finish();
                    }

                })
                .setNegativeButton("No, quiero seguir jugando", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.resume();
                    }
                })
                .show();
    }


}
