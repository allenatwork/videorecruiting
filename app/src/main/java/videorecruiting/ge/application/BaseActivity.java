package videorecruiting.ge.application;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.Vector;

import videorecruiting.ge.application.utils.AndroidUtils;


public class BaseActivity extends AppCompatActivity {
    private final String Tag = BaseActivity.class.getSimpleName();
    protected boolean mPaused;
    private final Vector<IKeyListener> mKeyListenerList = new Vector<IKeyListener>();

    private ProgressDialog mProgressDialog;

    public void showDialogRequesting (){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.waiting));
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();;
    }

    public void hideDialogRequesting (){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundleFromIntent();
    }

    protected void initBundleFromIntent() {

    }

    protected void initView (){

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        mPaused = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
            AndroidUtils.cleanView(view);
        }
        catch (Exception ex){

        }
    }

    /**
     * Call back when user pressed Back Button Device
     */
    public interface IKeyListener {
        boolean handleOnBackPress();

        boolean handleOnKeyUp(int keyCode, KeyEvent event);

        boolean handleOnKeyDown(int keyCode, KeyEvent event);

        boolean handleDispatchKeyEvent(KeyEvent event);
    }

    public void removeKeyBackCallBack(IKeyListener pIKeyListener) {
        this.mKeyListenerList.remove(pIKeyListener);
    }

    public void addKeyBackCallBack(IKeyListener pIKeyListener) {
        this.mKeyListenerList.add(pIKeyListener);
    }

    @Override
    public void onBackPressed() {
        // Disable default behavior of Back Button.
        // super.onBackPressed();
        if (!mKeyListenerList.isEmpty()) {
            for (IKeyListener pKeyBackCallBack : mKeyListenerList) {
                if (pKeyBackCallBack != null) {
                    pKeyBackCallBack.handleOnBackPress();
                }
            }
        } else {
            Log.e("onBackPressed", "Call back is empty");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mKeyListenerList.isEmpty()) {
            for (IKeyListener pKeyBackCallBack : mKeyListenerList) {
                if (pKeyBackCallBack != null && pKeyBackCallBack.handleOnKeyDown(keyCode, event)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

//    public abstract FragmentBackStack getFragmentBackStack();
}
