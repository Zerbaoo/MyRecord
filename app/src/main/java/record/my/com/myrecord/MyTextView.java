package record.my.com.myrecord;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Cody on 2016/12/30.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_PRESSED = 1;
    private int currentState = STATE_NORMAL;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
