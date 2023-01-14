package bruzsa.laszlo.dartsapp.ui.cricket;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class CricketTable extends View {

    private static final String TAG = "CricketTable";
    private Map<Integer, Integer> playerScores = new HashMap<>();
    private Map<Integer, Integer> opponentScores = new HashMap<>();

    public static final List<Integer> allNumbers = List.of(20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5);
    public static final List<Integer> activeNumbers = List.of(15, 16, 17, 18, 19, 20, 25);
    private final Paint paint = new Paint();
    private final RectF oval = new RectF();
    public float radius;
    private float radiusOfBull;
    private static final int circleMargin = 50;
    private float circleCentreX;
    private float circleCentreY;

    private int touchedValue;
    private static final float DEGREE = 18;


    public void refreshState(Map<Integer, Integer> playerScores, Map<Integer, Integer> opponentScores) {
        this.playerScores = playerScores;
        this.opponentScores = opponentScores;
        invalidate();
    }

    public CricketTable(Context context) {
        super(context);
    }

    public CricketTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CricketTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getLayoutParams().width = heightMeasureSpec;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setAntiAlias(true);

        radius = Math.min(getWidth(), getHeight()) / 2f - circleMargin;
        circleCentreY = circleCentreX = radius + circleMargin;
        radiusOfBull = radius / 4;

        oval.set(circleMargin, circleMargin, radius * 2 + circleMargin, radius * 2 + circleMargin);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(WHITE);
        canvas.drawPaint(paint);

        paint.setColor(GRAY);
        canvas.drawCircle(circleCentreX, circleCentreY, radius, paint);

        for (int i = 0; i < 20; i++) {
            int actual = allNumbers.get(i);
            if (activeNumbers.contains(actual)) {
                paint.setColor(getColorByValue(actual));
            } else {
                paint.setColor(GRAY);
            }
            canvas.drawArc(oval, -99 + i * DEGREE, DEGREE, true, paint);

        }

        // BULL
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getColorByValue(25));
        canvas.drawCircle(circleCentreX, circleCentreY, radiusOfBull, paint);

        drawStyleCircles(canvas);

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

    }

    @SuppressWarnings("ConstantConditions")
    private int getColorByValue(int actual) {
        if (!playerScores.containsKey(actual) || (playerScores.containsKey(actual) && playerScores.get(actual) < 3))
            if (!opponentScores.containsKey(actual) || (opponentScores.containsKey(actual) && opponentScores.get(actual) < 3))
                return BLUE;
            else
                return RED;
        else if (!opponentScores.containsKey(actual) || (opponentScores.containsKey(actual) && opponentScores.get(actual) < 3))
            return GREEN;
        else return BLACK;

    }

    private void drawStyleCircles(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(BLACK);
        canvas.drawCircle(circleCentreX, circleCentreY, radius / 4, paint);
        canvas.drawCircle(circleCentreX, circleCentreY, radius, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, String.format("onTouchEvent(%d): x: %.2f, y:%.2f", event.getAction(),event.getX(),event.getY()));
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;

        double distance = Math.sqrt(Math.pow(event.getX() - circleCentreX, 2) + Math.pow(event.getY() - circleCentreY, 2));
        if (distance <= radiusOfBull) {
            touchedValue = 25;
            performClick();
            return true;
        }
        if (distance <= radius * 0.65) {
            return false;
        }

        final double theta = Math.toDegrees(Math.atan2(event.getY() - circleCentreY, event.getX() - circleCentreX));
        for (int i = 0; i < 20; i++) {
            int min = -99 + i * 18;
            int max = -99 + i * 18 + 18;
            if (min <= theta && theta < max && activeNumbers.contains(allNumbers.get(i))) {
                touchedValue = allNumbers.get(i);
                performClick();
                return true;
            }
        }
        return false;
    }

    public int getTouchedValue() {
        return touchedValue;
    }
}
