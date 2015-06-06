package cl.dsalazarl.matchlearn;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            this.question = dividendo+":"+divisor;
            int cuociente, resto;
//            int cuociente_correct = dividendo/divisor;
//            int resto = dividendo-divisor*cuociente_correct;
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
}
