
package demo.tensorflow.org.sole_search;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import demo.tensorflow.org.sole_search.Classifier.Recognition;

import java.util.List;

public class RecognitionScoreView extends View implements ResultsView {
    private static final float TEXT_SIZE_DIP = 24;
    private List<Recognition> results;
    private final float textSizePx;
    private final Paint fgPaint;

    public RecognitionScoreView(final Context context, final AttributeSet set) {
        super(context, set);

        textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        fgPaint = new Paint();
        fgPaint.setTextSize(textSizePx);
    }

    @Override
    public void setResults(final List<Recognition> results) {
        this.results = results;
        postInvalidate();
    }

    @Override
    public void onDraw(final Canvas canvas) {

        fgPaint.setColor(Color.WHITE);

        if (results != null && results.size() > 0) {
            int y = (int) (fgPaint.getTextSize() * 1.4f);
            final Recognition recog = results.get(0);
            final int x = (int)(canvas.getWidth() - fgPaint.measureText(recog.getTitle())) / 2;
            canvas.drawText(recog.getTitle(), x, y, fgPaint);
        }
    }
}
