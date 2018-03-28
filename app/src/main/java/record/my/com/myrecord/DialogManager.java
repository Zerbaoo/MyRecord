package record.my.com.myrecord;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Cody on 2016/12/30.
 */


public class DialogManager {
    private static final String TAG="DialogManager";
    private Dialog mDialog;
    private Context mContext;
    ImageView mIcon;
    ImageView mVolume;
    TextView mLabel;
    public DialogManager(Context context) {
        mContext = context;

    }

    public void showDialog() {
        mDialog = new Dialog(mContext, R.style.AudioDialogTheme);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_record, null);
        mIcon = mView.findViewById(R.id.iv_record_icon);
        mVolume = mView.findViewById(R.id.iv_record_volume);
        mLabel = mView.findViewById(R.id.tv_record_dialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ConvertUtils.dp2px(166), ConvertUtils.dp2px(166));
        mDialog.setContentView(mView, params);
        countDown();
        mDialog.show();
    }


    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVolume.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mVolume.setImageResource(R.drawable.v1);
            mLabel.setText(R.string.str_recorder_cancel);
            mLabel.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    public void readyCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVolume.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.drawable.cancel);
            mLabel.setText(R.string.str_recorder_cancel_confirm);
            mLabel.setBackgroundColor(Color.parseColor("#00BCD4"));
        }


    }


    public void durationTooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVolume.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLabel.setText(R.string.str_recorder_duration_too_short);
            mLabel.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    /**
     * 通过level去更新voice上的图片
     */

    public void updateVolume(int level) {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVolume.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);
            /*switch (level){
                case 1:
                    mVolume.setImageResource(R.drawable.v1);
                    break;
                case 2:
                    mVolume.setImageResource(R.drawable.v2);
                    break;
                case 3:
                    mVolume.setImageResource(R.drawable.v3);
                    break;
                case 4:
                    mVolume.setImageResource(R.drawable.v4);
                    break;
                case 5:
                    mVolume.setImageResource(R.drawable.v5);
                    break;
                case 6:
                    mVolume.setImageResource(R.drawable.v6);
                    break;
                case 7:
                    mVolume.setImageResource(R.drawable.v7);
                    break;
            }*/
//            int resId = mContext.getResources()
//                    .getIdentifier("v" + level, "drawable", mContext.getPackageName());
//            mVolume.setImageResource(resId);

            int resId = mContext.getResources().getIdentifier("v" + level, "drawable", mContext.getPackageName());
            mVolume.setImageResource(resId);
        }
    }

    /** 倒计时秒 */
    private int time = LIMIT_TIME;
    public static final int LIMIT_TIME = 20;//指定最大录制时长
    Timer timer ;
    /** 倒计时 */
    private void countDown() {
            if(timer != null){
                timer = null;
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("DialogManager","time: " + (time));
                    if(time<=0){
                        timer.cancel();
                        time = LIMIT_TIME;
                        return;
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }

                }
            }, 1000, 1000);
    }

    @SuppressLint("HandlerLeak")
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(time <= 10){
                        mLabel.setText("您还可以说 " + Integer.toString(time) + " 秒");
                        mLabel.setBackgroundColor(Color.TRANSPARENT);
                    }
                    time--;
                    if(time == 0){
                        time = LIMIT_TIME;
                    }
                    break;

            }
        }
    };

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
            timer.cancel();
            time = LIMIT_TIME;
        }
    }
}

