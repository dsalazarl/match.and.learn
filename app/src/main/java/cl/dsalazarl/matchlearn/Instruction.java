package cl.dsalazarl.matchlearn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Instruction extends Activity{

    Button startButton;
    TextView NombreApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_lay);

        startButton = (Button) findViewById(R.id.btn_comenzar);

        NombreApp = (TextView) findViewById(R.id.text_nombre_app);
        Typeface HelveticaItalica= Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue-UltraLightItal.otf");
        NombreApp.setTypeface(HelveticaItalica);
    }

    public void startGame(View v) {

        Intent main = new Intent(this, MainActiviti.class);
        startActivity(main);

        Intent Play = new Intent(this, MainActivity.class);
        startActivity(Play);
        Instruction.this.finish();
    }
    @Override
    public void onBackPressed() {
        Intent Back = new Intent(this, MainActiviti.class);
        startActivity(Back);

        Instruction.this.finish();
    }

}