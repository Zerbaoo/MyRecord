package record.my.com.myrecord;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by lijix on 2018/3/26.
 */

public class RecorderPopupWindow  extends BasePopupWindow implements View.OnClickListener{

    private View popupView;
    private OnPopupClickListener mOnPopupClickListener;

    public RecorderPopupWindow(Activity context, int width, int height) {
        super(context, width, height);
        bindEvent();
    }

    @Override
    public View getInputView() {
        return null;
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.item_pop, null);
        return popupView;
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_favor).setOnClickListener(this);
            popupView.findViewById(R.id.tv_delete).setOnClickListener(this);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_favor:
                if (mOnPopupClickListener != null) {
                    mOnPopupClickListener.onLikeClick(v);
                    dismiss();
                }
                break;
            case R.id.tv_delete:
                if (mOnPopupClickListener != null) {
                    mOnPopupClickListener.onDeleteClick(v);
                    dismiss();
                }
                break;
        }
    }

    public interface OnPopupClickListener {
        void onLikeClick(View v);

        void onDeleteClick(View v);
    }

    public void setOnPopupClickListener(OnPopupClickListener onClickListener) {
        mOnPopupClickListener = onClickListener;
    }

    @Override
    public void showPopupWindow(View view) {
        super.showPopupWindow(view);
    }

}
