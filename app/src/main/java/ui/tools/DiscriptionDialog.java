package ui.tools;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolwq.R;


/**
 * Created by Y-GH on 2017/5/12.
 */

public class DiscriptionDialog extends DialogFragment {


    // implements this interface if you want to create the dialog
    public interface LoginDialogCreater {
        LoginDialogListener getLoginDialogListener();
    }

    public interface LoginDialogListener {
        public void getNameAddr(String username);

    }

    LoginDialogListener dialogListener = null;

    /**********
     * 通过这个方法，将回调函数的地址传入到MyDialog中
     *********/
    public void setDialogListener(LoginDialogListener listener) {
        this.dialogListener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("详情");
        getDialog().setCanceledOnTouchOutside(true);
        final View dialogView = inflater.inflate(R.layout.dialog_desc, container);
        final TextView Text = ((TextView) dialogView.findViewById(R.id.discription));
        Text.setText(getTag());
        dialogView.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return dialogView;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
