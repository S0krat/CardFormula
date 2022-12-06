package app.cardformula;

import android.content.Context;
import android.graphics.Point;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Card extends androidx.appcompat.widget.AppCompatTextView {

    private final ConstraintLayout.LayoutParams layoutParams;
    public int index;

    public Card(Context context) {
        super(context);
        this.setTextColor(getResources().getColor(R.color.black));
        this.setBackgroundResource(R.drawable.card_selector);
        this.setTextSize(50);
        this.setPadding(50, 0, 50, 10);
        layoutParams = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        this.setLayoutParams(layoutParams);
    }

    public void setPosition(int x, int y) {
        layoutParams.setMargins(x, y, 0, 0);
        this.setLayoutParams(layoutParams);
    }

    public Point getPosition() {
        return new Point(((ViewGroup.MarginLayoutParams)layoutParams).leftMargin,
                ((ViewGroup.MarginLayoutParams)layoutParams).topMargin);
    }
}
