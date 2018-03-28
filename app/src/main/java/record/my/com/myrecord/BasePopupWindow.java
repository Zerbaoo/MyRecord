package record.my.com.myrecord;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by lijix on 2018/3/26.
 */

public abstract class BasePopupWindow implements BasePopup{

    protected PopupWindow mPopupWindow;
    protected View mContentView;
    protected Activity mContext;
    protected boolean showKeyboard = false;
    private OnDismissListener mOnDismissListener;

    public BasePopupWindow(Activity context) {
        mContext = context;
        mContentView = getPopupView();
        mContentView.setFocusable(true);
        mContentView.setFocusableInTouchMode(true);
        //默认全屏
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //指定背景透明
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        //无动画
        mPopupWindow.setAnimationStyle(0);
    }


    public BasePopupWindow(Activity context, int width, int height) {
        mContext = context;
        mContentView = getPopupView();
        mContentView.setFocusable(true);
        mContentView.setFocusableInTouchMode(true);
        //默认全屏
        mPopupWindow = new PopupWindow(mContentView, ConvertUtils.dp2px(width), ConvertUtils.dp2px(height));
        //指定背景透明
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        //无动画
        mPopupWindow.setAnimationStyle(0);
    }


    /**
     * 抽象方法
     */

    public abstract View getInputView();


    public Context getContext() {
        return this.mContext;
    }

    /**
     * 显示PopWindow
     */


    public void showPopupWindow() {
        mPopupWindow.showAtLocation(mContext.findViewById(android.R.id.content), Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void showPopupWindow(View view) {
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM , 0, view.getHeight());
    }


    public void show(View view) {
        mPopupWindow.showAsDropDown(view);
    }


    public void show(View view,int offsetX,int offsetY) {
        mPopupWindow.showAsDropDown(view,offsetX,offsetY);
    }


    public void showPopupWindow(int resId) {
        mPopupWindow.showAtLocation(mContext.findViewById(resId), Gravity.RIGHT | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    public void setBackPressEnable(boolean backPressEnable){
        if (backPressEnable){
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        }else {
            mPopupWindow.setBackgroundDrawable(null);
        }
    }


    public boolean isShowing(){
        return mPopupWindow.isShowing();
    }

    public OnDismissListener getOndismissListener(){
        return mOnDismissListener;
    }


    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        if (mOnDismissListener != null) {
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mOnDismissListener.onDismiss();
                }
            });
        }
    }
    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public interface OnDismissListener {
        void onDismiss();
    }

}
