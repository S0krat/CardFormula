package app.cardformula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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
        back_btn.setOnClickListener(v -> finish());

        ArrayList<Button> buttons = new ArrayList<>();

        buttons.add(findViewById(R.id.lvl1_btn));
        buttons.add(findViewById(R.id.lvl2_btn));
        buttons.add(findViewById(R.id.lvl3_btn));
        buttons.add(findViewById(R.id.lvl4_btn));
        buttons.add(findViewById(R.id.lvl5_btn));
        buttons.add(findViewById(R.id.lvl6_btn));
        buttons.add(findViewById(R.id.lvl7_btn));
        buttons.add(findViewById(R.id.lvl8_btn));

        for (int i = 0; i < 8; i++) {
            buttons.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, Level.class);
        intent.putExtra("level", ((Button)view).getText().toString());
        intent.putExtra("complex", complexity);
        startActivity(intent);
    }
}