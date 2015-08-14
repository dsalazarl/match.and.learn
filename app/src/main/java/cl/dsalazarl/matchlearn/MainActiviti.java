package cl.dsalazarl.matchlearn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActiviti extends Activity{

    Button playButton;
    Button infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lay);

        playButton = (Button) findViewById(R.id.btn_play);
        infoButton = (Button) findViewById(R.id.btn_info);
    }

    public void ToGame(View v) {
        Intent Play = new Intent(this, MainActivity.class);
        startActivity(Play);
    }

    public void ToInfo(View v) {
        Intent Info = new Intent(this, Instruction.class);
        startActivity(Info);
        MainActiviti.this.finish();
    }
    @Override
    public void onBackPressed() {
        MainActiviti.this.finish();
    }

}