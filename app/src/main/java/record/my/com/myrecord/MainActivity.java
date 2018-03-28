package record.my.com.myrecord;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.*;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private int mMinItemWidth;
    private int mMaxItemWidth;
    private Context mContext;
    AudioRecordButton bt_speak;
    private TextView tv_chatbox;
    private ImageView iv_record;
    private TextView tv_duration;
    private RelativeLayout rl_raidoview;
    private RecorderPopupWindow mWindow;
    private String RecordFile;
    private Button filetobase64;
    MediaPlayer mMediaPlayer;
    private TextView tv_duration1;
    private TextView tv_chatbox1;
    private ImageView iv_record1;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UtilsP.verifyPermissions(this);

        tv_duration1 = findViewById(R.id.tv_duration1);
        bt_speak = findViewById(R.id.bt_speak);
        tv_chatbox = findViewById(R.id.tv_chatbox);
        tv_chatbox1 = findViewById(R.id.tv_chatbox1);
        iv_record1 = findViewById(R.id.iv_record1);
        iv_record = findViewById(R.id.iv_record);
        filetobase64 = findViewById(R.id.filetobase64);
        tv_duration = findViewById(R.id.tv_duration);
        rl_raidoview = findViewById(R.id.rl_raidoview);
        rl_raidoview.setVisibility(View.GONE);
        mWindow = new RecorderPopupWindow(this, 128, 50);
        mWindow.setOnPopupClickListener(new RecorderPopupWindow.OnPopupClickListener() {
            @Override
            public void onLikeClick(View v) {

            }

            @Override
            public void onDeleteClick(View v) {
                if(!TextUtils.isEmpty(RecordFile)){
                    File file=new File(RecordFile);
                    file.delete();
                    rl_raidoview.setVisibility(View.GONE);
                }

                   
            }
        });
        initData();

        filetobase64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(RecordFile)){

                    try {
                        String base  = FileUtils.encodeBase64File(RecordFile);
                        String fileName = generateFileName();
                        String dir = getExternalFilesDir("osp_user_audios").getAbsolutePath();
                        //String dir = Environment.getExternalStorageDirectory() + "/osp_audios";
                        File fileDir = new File(dir);
                        if (!fileDir.exists()) {
                            fileDir.mkdir();
                        }

                        final File file = new File(dir, fileName);
                        FileUtils.decoderBase64File(base ,file.getAbsolutePath());
                        if(file.exists()){
                            filetobase64.setText(file.getAbsolutePath());

                            if (mMediaPlayer == null) {
                                mMediaPlayer = new MediaPlayer();
                                //播放错误 防止崩溃
                                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        mMediaPlayer.reset();
                                        return false;
                                    }
                                });
                            } else {
                                mMediaPlayer.reset();
                            }

                            try {
                                mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
                                mMediaPlayer.setDataSource(file.getAbsolutePath());
                                mMediaPlayer.prepare();
                                float duration = mMediaPlayer.getDuration();
                                Log.e("Main" ,"duration: " +duration);
                                Log.e("Main" ,"duration/1000: " +Math.round(duration/1000));
                                ViewGroup.LayoutParams params = tv_chatbox1.getLayoutParams();
                                //动态改变chatbox的长度
                                params.width = (int) (mMinItemWidth + mMaxItemWidth / 60f * Math.round(duration/1000));
                                iv_record1.setBackgroundResource(R.drawable.adj);
                                tv_duration1.setText(Math.round(duration/1000) + "\"");
                                tv_chatbox1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (iv_record1 != null) {
                                            iv_record1.setBackgroundResource(R.drawable.adj);
                                        }

                                        iv_record1.setBackgroundResource(R.drawable.play_anim);
                                        AnimationDrawable anim = (AnimationDrawable) iv_record1.getBackground();
                                        anim.start();
                                        //播放音频
                                        MediaManager.playSound(file.getAbsolutePath(), new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                iv_record1.setBackgroundResource(R.drawable.adj);
                                            }
                                        });
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        scrollView = findViewById(R.id.scrollView);
    }


    private String generateFileName() {
        return UUID.randomUUID().toString() +"_osp"+ ".amr";
    }


    private void initData() {


        mContext = getApplicationContext() ;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.2f);

        bt_speak.setDoneRecordListener(new AudioRecordButton.DoneRecordListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Log.e("Main" ,"filePath: " +filePath);
                RecordFile = filePath;
                rl_raidoview.setVisibility(View.VISIBLE);
                int duration = Math.round(seconds);
                ViewGroup.LayoutParams params = tv_chatbox.getLayoutParams();
                //动态改变chatbox的长度
                params.width = (int) (mMinItemWidth + mMaxItemWidth / 60f * duration);
                iv_record.setBackgroundResource(R.drawable.adj);
                tv_duration.setText(duration + "\"");
            }
        });

        tv_chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Main" ,"tv_chatbox:onClick");
                if (iv_record != null) {
                    iv_record.setBackgroundResource(R.drawable.adj);
                }


                iv_record.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim = (AnimationDrawable) iv_record.getBackground();
                anim.start();

                Log.e("Main" ,"anim:start");
                //播放音频
                MediaManager.playSound(RecordFile, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        Log.e("Main" ,"onCompletion");
                        iv_record.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });


        tv_chatbox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mWindow.show(v, ConvertUtils.dp2px(-50), -v.getHeight()*2);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

}
