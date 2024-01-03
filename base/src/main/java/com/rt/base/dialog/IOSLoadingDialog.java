package com.rt.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rt.base.R;


/**
 * Created by OnionMac on 17/9/22.
 */

public class IOSLoadingDialog extends Dialog {

    private boolean mMessageGone = false;

    public IOSLoadingDialog(Context context) {
        super(context);
    }

    public IOSLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setMessage(String tips) {
        TextView msgView = (TextView) findViewById(R.id.tipTextView);
//        msgView.setVisibility(View.VISIBLE);
        msgView.setText(tips);
    }

    public void MessageGone() {
        TextView msgView = (TextView) findViewById(R.id.tipTextView);
        msgView.setVisibility(View.GONE);
    }

    public static class Builder {

        private Context context;
        private String message;
        private boolean isShowMessage = true;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public IOSLoadingDialog create() {

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.ios_loading_layout, null);
            IOSLoadingDialog loadingDailog = new IOSLoadingDialog(context, R.style.ios_dialog_style);
            TextView msgText = (TextView) view.findViewById(R.id.tipTextView);
            if (isShowMessage) {
                msgText.setText(message);
            } else {
                msgText.setVisibility(View.GONE);
            }

            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;

        }


    }
}
