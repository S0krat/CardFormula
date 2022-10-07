package app.cardformula;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Level extends AppCompatActivity {

    private String level;
    private String complexity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        Bundle arguments = getIntent().getExtras();
        TextView textView = findViewById(R.id.lvl_text);
        level = arguments.getString("level").toString();
        complexity = arguments.getString("complex").toString();
        textView.setText(complexity + ". Level: " + level);

        final Button back_btn = findViewById(R.id.lvl_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}