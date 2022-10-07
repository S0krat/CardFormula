package app.cardformula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LevelChoose extends AppCompatActivity implements View.OnClickListener{

    private String complexity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_choose);

        Bundle arguments = getIntent().getExtras();
        TextView textView = findViewById(R.id.lvl_list_text);
        complexity = arguments.getString("complex").toString();
        textView.setText(complexity);

        final Button back_btn = findViewById(R.id.lvl_choose_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.lvl1_btn).setOnClickListener(this);
        findViewById(R.id.lvl2_btn).setOnClickListener(this);
        findViewById(R.id.lvl3_btn).setOnClickListener(this);
        findViewById(R.id.lvl4_btn).setOnClickListener(this);
        findViewById(R.id.lvl5_btn).setOnClickListener(this);
        findViewById(R.id.lvl6_btn).setOnClickListener(this);
        findViewById(R.id.lvl7_btn).setOnClickListener(this);
        findViewById(R.id.lvl8_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, Level.class);
        intent.putExtra("level", ((Button)view).getText().toString());
        intent.putExtra("complex", complexity);
        startActivity(intent);
    }
}