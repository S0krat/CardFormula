package app.cardformula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Level extends AppCompatActivity implements View.OnTouchListener {

    private int dragX = 0, dragY = 0;
    private final int cardSize = 200;
    private int cardsNum;
    private Point screenSize, nPrev;
    private ArrayList<Card> deck;
    private ArrayList<Point> allowedPoints;
    private ArrayList<Card> board;
    private Card ghostCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deck = new ArrayList<>();
        allowedPoints = new ArrayList<>();
        board = new ArrayList<>();
        ghostCard = new Card(this);
        ghostCard.setAlpha(0f);
        nPrev = new Point(0, 0);
        // Создание главного layout
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setBackgroundResource(R.drawable.main_bg);
        // Создание кнопки "назад"
        Button back_btn = new Button(this);
        back_btn.setId(R.id.lvl_back_btn);
        back_btn.setText(R.string.back_btn);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        back_btn.setOnClickListener(v -> finish());

//        char[] char_eq = "2+3=4".toCharArray();
//        if (Calculator.Compare(char_eq)) {
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "Все получилось!", Toast.LENGTH_SHORT);
//            toast.show();
//        } else {
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "Не получилось(((((((((", Toast.LENGTH_SHORT);
//            toast.show();
//        }


        back_btn.setLayoutParams(layoutParams);
        constraintLayout.addView(back_btn);
        // Взятие аргументов предыдущей activity
        Bundle arguments = getIntent().getExtras();
        String level = arguments.getString("level");
        String complexity = arguments.getString("complex");
        // Создание строки - полное название уровня
        String levelName = "complex";
        switch (complexity) {
            case "1-4 класс":
                levelName += "1_level";
                break;
            case "5-9 класс":
                levelName += "2_level";
                break;
            case "10-11 класс":
                levelName += "3_level";
                break;
            case "1-2 курс":
                levelName += "4_level";
                break;
        }
        levelName += level;
        // Создание массива карточек
        String[] cards = getResources().getStringArray
                (getResources().getIdentifier(levelName, "array", this.getPackageName()));
        cardsNum = cards.length;
        ghostCard.index = cardsNum;
        // Получение размеров экрана
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(screenSize);
        // Создание карточек на экране
        for (int i = 0; i < cardsNum; i++) {
            Card card = new Card(this);
            card.setText(cards[i]);
            card.setOnTouchListener(this);
            card.index = i;
            deck.add(card);
            constraintLayout.addView(card);
        }

        //ghostCard.setAlpha(0f);
        constraintLayout.addView(ghostCard);
        allowedPoints.add(new Point((screenSize.x - cardSize) / 2, screenSize.y / 4));
        DeckRedistribute();
        setContentView(constraintLayout);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        Card card = (Card)v;
        Point point = card.getPosition();
        //v.performClick();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dragX = x;
                dragY = y;
                ChangeGhostCard(ghostCard, card);
                if (deck.contains(card)) {
                    card.setRotation(0);
                    deck.remove(card);
                } else {
                    board.remove(card);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                card.setPosition(point.x + x - dragX, point.y + y - dragY);
                if (point.y > 0.5 * screenSize.y) {
                    if (board.contains(ghostCard)) {
                        board.remove(ghostCard);
                        BoardRedistribute();
                        UpdateAllowedPoints();
                    }
                    if (!deck.contains(ghostCard)) {
                        deck.add(ghostCard);
                        DeckRedistribute();
                    }
                } else {
                    Point np = GetNearestPoint(point);
                    if (deck.contains(ghostCard)) {
                        deck.remove(ghostCard);
                        DeckRedistribute();
                    }
                    if (!board.contains(ghostCard)) {
                        ghostCard.setRotation(0f);
                        ghostCard.setPosition(np.x, np.y);
                        AddCardToBoard(ghostCard);
                        BoardRedistribute();
                        UpdateAllowedPoints();
                    }
                    if (!np.equals(nPrev)) {
                        nPrev = np;
                        board.remove(ghostCard);
                        ghostCard.setPosition(np.x, np.y);
                        AddCardToBoard(ghostCard);
                        BoardRedistribute();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                deck.remove(ghostCard);
                board.remove(ghostCard);
                ghostCard.setAlpha(0);
                if (point.y > 0.5 * screenSize.y) {
                    deck.add(card);
                    DeckRedistribute();
                } else {
                    Point pos = GetNearestPoint(point);
                    card.setPosition(pos.x, pos.y);
                    AddCardToBoard(card);
                }
                BoardRedistribute();
                UpdateAllowedPoints();
                if (CheckEquation()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                        "Уровень пройден!", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                break;
        }

        return true;
    }

    private Point GetNearestPoint(Point pos) {
        Point np = null;
        double dist, minDist = screenSize.x;
        for (Point p : allowedPoints) {
            dist = Math.sqrt((pos.x - p.x) * (pos.x - p.x) + (pos.y - p.y) * (pos.y - p.y));
            if (dist < minDist) {
                np = p;
                minDist = dist;
            }
        }
        return np;
    }

    private void DeckRedistribute() {
        int size = deck.size();
        int space = screenSize.x / (size + 1);
        int j = 0;
        if (size == 1) {
            deck.get(0).setPosition((screenSize.x - cardSize) / 2, (int)(0.75f * screenSize.y));
            return;
        }
        for (int i = 0; i <= cardsNum; i++) {
            for (Card c : deck) {
                if (c.index == i) {
                    c.setPosition(space + j * space - cardSize / 2,
                            (int)(0.75f * screenSize.y - 150 * Math.sin(Math.PI / (size - 1) * j)));
                    c.setRotation(-7.5f * (size - 1) + 15 * j);
                    j++;
                    break;
                }
            }
        }
    }

    private void AddCardToBoard(Card card) {
        Point cardPos = card.getPosition();
        int size = board.size();
        if (size == 0) {
            board.add(card);
        } else {
            for (int i = 0; i < size; i++) {
                Point pos = board.get(i).getPosition();
                if (pos.x > cardPos.x && pos.y == cardPos.y) {
                    board.add(i, card);
                    return;
                }
            }
            board.add(card);
        }
    }

    private void UpdateAllowedPoints() {
        allowedPoints.clear();
        int size = board.size();
        for (int i = 0; i < size; i++) {
            Card card = board.get(i);
            Point pos = card.getPosition();
            pos.x -= cardSize / 2;
            allowedPoints.add(pos);
            if (i + 1 == size) {
                Point pos1 = card.getPosition();
                pos1.x += cardSize/2;
                allowedPoints.add(pos1);
            }
            if (size > 5 && i + 1 == size / 2) {
                Point pos1 = card.getPosition();
                pos1.x += cardSize/2;
                allowedPoints.add(pos1);
            }
        }
        System.out.println(allowedPoints);
        if (allowedPoints.size() == 0){
            allowedPoints.add(new Point((screenSize.x - cardSize) / 2, screenSize.y / 4));
        }
    }

    private void BoardRedistribute() {
        int size = board.size();
        if (size <= 5) {
            for (int i = 0; i < size; i++) {
                Card card = board.get(i);
                card.setPosition((screenSize.x - cardSize * (size - 2 * i)) / 2, screenSize.y / 4);
            }
        } else {
            for (int i = 0; i < size / 2; i++) {
                Card card = board.get(i);
                card.setPosition((screenSize.x - cardSize * (size / 2 - 2 * i)) / 2, screenSize.y / 4 - 150);
            }
            for (int i = size / 2; i < size; i++) {
                Card card = board.get(i);
                card.setPosition((screenSize.x - cardSize * (size / 2 + size % 2 - 2 * (i - size / 2))) / 2, screenSize.y / 4 + 150);
            }
        }
    }

    private void ChangeGhostCard(@NonNull Card ghostCard, @NonNull Card card) {
        ghostCard.setText(card.getText());
        Point p = card.getPosition();
        ghostCard.setPosition(p.x, p.y);
        ghostCard.setAlpha(0.5f);
    }

    private boolean CheckEquation() {
        char[] cards = new char[board.size()];
        int i = 0;
        for (Card c : board) {
            cards[i] = c.getText().charAt(0);
            i++;
        }
        return Calculator.Compare(cards);
    }
}