package app.cardformula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.complex1_btn).setOnClickListener(this);
        findViewById(R.id.complex2_btn).setOnClickListener(this);
        findViewById(R.id.complex3_btn).setOnClickListener(this);
        findViewById(R.id.complex4_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, LevelChoose.class);
        intent.putExtra("complex", ((Button)view).getText().toString());
        startActivity(intent);
    }
}