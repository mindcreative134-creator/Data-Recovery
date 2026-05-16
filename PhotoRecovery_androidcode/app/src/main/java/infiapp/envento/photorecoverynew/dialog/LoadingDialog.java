package infiapp.envento.photorecoverynew.dialog;

import android.app.Dialog;
import android.content.Context;
import com.recovery.photodeleted.data.R;

public class LoadingDialog extends Dialog {

    private Context mContext;

    public LoadingDialog(Context activity) {
        // Using standard AppCompat dialog style
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog);
        this.mContext = activity;
        requestWindowFeature(1);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_loading_dialog);
    }
}