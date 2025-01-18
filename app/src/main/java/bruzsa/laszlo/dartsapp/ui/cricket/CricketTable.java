package bruzsa.laszlo.dartsapp.ui.cricket;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Collections.emptyList;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.cricket.CricketSettings.BULL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.cricket.Stat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ViewConstructor")
public class CricketTable extends View {

    private static final String TAG = "CricketTable";
    private static final float CLICK_PERCENT = 0.4f;
    public static final List<Integer> ALL_NUMBERS = List.of(20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5);
    private List<Integer> activeNumbers = CricketSettings.defaultNumbers;
    private final Paint paint = new Paint();
    private final RectF oval = new RectF();
    private float radius;
    private float radiusOfBull;
    private static final int CIRCLE_MARGIN = 10;
    private float circleCentreX;
    private float circleCentreY;

    private Size size;
    @Getter
    @Setter
    private Team team = TEAM1;
    @Setter
    private Stat stat = new Stat(emptyList(), activeNumbers);

    @RequiredArgsConstructor
    @Getter
    public static class TouchValue {
        private final int multiplier;
        private final int value;
    }

    public static final int INVALID = -1;
    private int lastTouchValue;

    private static final int DEGREE = 18;
    private static final int OFFSET_DEGREE = -99;

    public void inic(CricketSettings settings, BiConsumer<Team, TouchValue> onTouchEventListener) {
        this.size = settings.getSize();
        activeNumbers = settings.getActiveNumbers();
        setOnClickListener(v -> onTouch(onTouchEventListener, 1));
        setOnLongClickListener(v -> {
            onTouch(onTouchEventListener, lastTouchValue == BULL ? 2 : 3);
            return true;
        });
    }

    private void onTouch(BiConsumer<Team, TouchValue> onTouchEventListener, int multiplier) {
        if (activeNumbers.contains(lastTouchValue))
            onTouchEventListener.accept(team, new TouchValue(multiplier, lastTouchValue));
    }

    public CricketTable(Context context) {
        super(context, null, 0);
    }

    public CricketTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CricketTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (size == null || size.getWidth() == 0 || size.getHeight() == 0) {
            int newSize = heightMeasureSpec - 20;
            setMeasuredDimension(newSize, newSize);
        } else {
            int newSize = (int) Math.min(size.getWidth() / 2.2f, size.getHeight());
            setMeasuredDimension(newSize, newSize);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        paint.setAntiAlias(true);

        Log.d(TAG, String.format("onDraw: width:%s1, height:%s1", getWidth(), getHeight()));
        radius = Math.min(getWidth(), getHeight()) / 2f - CIRCLE_MARGIN;
        circleCentreY = radius + CIRCLE_MARGIN;
        radiusOfBull = radius / 4;

        if (team == TEAM1) {
            circleCentreX = radius + CIRCLE_MARGIN;
            oval.set(CIRCLE_MARGIN,
                    CIRCLE_MARGIN,
                    radius * 2 + CIRCLE_MARGIN,
                    radius * 2 + CIRCLE_MARGIN);
        } else {
            circleCentreX = getWidth() - radius - CIRCLE_MARGIN;
            float leftX = getWidth() / 2f - circleCentreY + CIRCLE_MARGIN;
            oval.set(leftX,
                    CIRCLE_MARGIN,
                    leftX + radius * 2,
                    radius * 2 + CIRCLE_MARGIN);
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(GRAY);
        canvas.drawCircle(circleCentreX, circleCentreY, radius, paint);

        activeNumbers.forEach(number -> {
            if (number == BULL) return;
            int position = ALL_NUMBERS.indexOf(number);
            paint.setColor(stat.getStateMap(team).get(number).getColor());
            canvas.drawArc(oval, OFFSET_DEGREE + position * DEGREE, DEGREE, true, paint);
        });


        // BULL
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(stat.getStateMap(team).get(BULL).getColor());
        canvas.drawCircle(circleCentreX, circleCentreY, radiusOfBull, paint);


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(BLACK);
        canvas.drawCircle(circleCentreX, circleCentreY, radius / 4, paint);
        canvas.drawCircle(circleCentreX, circleCentreY, radius, paint);


        final int SIZE = (int) (radius / 10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        Map<Integer, Integer> statMap = stat.getStatMap().get(team);
        activeNumbers.forEach(number -> {
            Integer count = statMap.get(number);
            if (count == null || count == 0 || count > 2) return;
            if (number == BULL) {
                if (count == 2) {
                    canvas.drawCircle(circleCentreX + SIZE, circleCentreY, SIZE, paint);
                }
                canvas.drawCircle(circleCentreX - SIZE, circleCentreY, SIZE, paint);
            } else {
                int position = ALL_NUMBERS.indexOf(number);
                if (count == 2) {
                    Point point = getPoint(position * DEGREE, (int) (radius - 3 * SIZE));
                    canvas.drawCircle(point.x, point.y, SIZE, paint);
                }
                Point point = getPoint(position * DEGREE, (int) (radius - SIZE));
                canvas.drawCircle(point.x, point.y, SIZE, paint);
            }
        });

    }

    public Point getPoint(int degree, int margin) {
        double radian = degree / (180 / Math.PI) - Math.PI / 2;
        final float s = (float) (margin * sin(0));
        final float t = (float) (margin * cos(0));
        return new Point(
                (int) ((-s * sin(radian) + t * cos(radian)) + circleCentreY),
                (int) ((s * cos(radian) + t * sin(radian)) + circleCentreX)
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event);
        Log.d(TAG, String.format("onTouchEvent(%d): x: %.0f, y:%.0f", event.getAction(), event.getX(), event.getY()));

        double distance = Math.sqrt(Math.pow(event.getX() - circleCentreX, 2) + Math.pow(event.getY() - circleCentreY, 2));
        if (distance <= radiusOfBull) {
            lastTouchValue = BULL;
            return super.onTouchEvent(event);
        }
        if (distance <= radius * CLICK_PERCENT || distance > radius) {
            lastTouchValue = INVALID;
            return true;
        }

        // calculate degree of touch
        double theta = Math.toDegrees(Math.atan2(event.getY() - circleCentreY, event.getX() - circleCentreX));
        if (theta < 0) theta += 360;

        int index = (int) (((theta + 99) % 360) / DEGREE);
        if (activeNumbers.contains(ALL_NUMBERS.get(index))) {
            lastTouchValue = ALL_NUMBERS.get(index);
            return super.onTouchEvent(event);
        } else {
            lastTouchValue = INVALID;
            return true;
        }
    }

    @BindingAdapter("state")
    public static void setState(View view, Stat stat) {
        if (view instanceof CricketTable table) {
            table.setStat(stat);
            table.invalidate();
        }
    }

}
