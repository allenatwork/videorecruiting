package videorecruiting.ge.application;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by thangtranquyet on 3/16/16.
 */
public class BaseFragment extends Fragment {
    private final String Tag = BaseFragment.class.getSimpleName();
    protected boolean mPaused;
    protected ProgressBar mProgressBar;
    protected View mLoadingDialog;
    protected View mLayoutFailed;
    protected Context mContext;
    private BaseActivity mActivity;

    private final BaseActivity.IKeyListener pIKeyListener = new BaseActivity.IKeyListener() {
        @Override
        public boolean handleOnBackPress() {
            if (isFragmentValid()) {
                onKeyBackPress();
            }
            return true;
        }

        @Override
        public boolean handleOnKeyUp(int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean handleOnKeyDown(int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean handleDispatchKeyEvent(KeyEvent event) {
            return false;
        }
    };

    protected final boolean isFragmentValid() {
        return isVisible() && isAdded() && isResumed() && !isRemoving() && !isDetached();
    }

    protected void onKeyBackPress() {
        if (isFragmentValid()) {
            Log.d("onKeyBackPress ", "ok");
            FragmentManager fm = getFragmentManager();

            if (fm.getBackStackEntryCount() != 0) {
                fm.popBackStack();
            } else {
                mActivity.finish();
            }

        } else {
            Log.e("onKeyBackPress ", "ignored");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mActivity.addKeyBackCallBack(pIKeyListener);
        }

        initParameterFromBundle();
    }

    @Override
    public void onDetach() {
        if (mActivity != null) {
            mActivity.removeKeyBackCallBack(pIKeyListener);
        }

        super.onDetach();
        mContext = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    protected void initParameterFromBundle (){

    }

    protected void initView (View rootView){
        if(rootView == null) return;
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        View mButtonClose = rootView.findViewById(R.id.buttonClose);
        if(mButtonClose != null){
            mButtonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        mLayoutFailed = rootView.findViewById(R.id.layoutFailed);
        if(mLayoutFailed != null){
            mLayoutFailed.setVisibility(View.GONE);
        }

    }


    protected void showView (View rootView){
        if(rootView == null) return;
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        View mButtonClose = rootView.findViewById(R.id.buttonClose);
        if (mButtonClose != null) {
            mButtonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        mLayoutFailed = rootView.findViewById(R.id.layoutFailed);
        if(mLayoutFailed != null){
            mLayoutFailed.setVisibility(View.GONE);
        }

    }

    protected void finishedView (boolean success){
        if(mProgressBar != null){
            mProgressBar.setVisibility(View.GONE);
        }

        if(success){
            if(mLayoutFailed != null){
                mLayoutFailed.setVisibility(View.GONE);
            }
        }
        else{
            if(mLayoutFailed != null){
                mLayoutFailed.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    /**
     * Run on main thread
     *
     * @param runnable
     */
    protected void runOnUiThread(Runnable runnable) {
        if (getActivity() == null || !isAdded()) {
            return;
        }
        getActivity().runOnUiThread(runnable);
    }

    /**
     * Find view by ID
     *
     * @param id
     * @return View
     */
    protected View findViewById(int id) {
        if (getView() != null) {
            return getView().findViewById(id);
        }
        return null;
    }

    /**
     * Show loading in fragment
     */
    public void showLoadingView() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getView() == null) {
                        return;
                    }
                    final View v = findViewById(R.id.loading_view);
                    if (v == null) {
                        return;
                    }
                    try {
                        if (isAdded()) {
                            v.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide loading in fragment
     */
    public void hideLoadingView() {
        try {
            final View v = findViewById(R.id.loading_view);;
            if (getView() == null) {
                return;
            }
            //v = view;
            if (v == null) {
                return;
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (isAdded()) {
                            v.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void switchFragment(BaseFragment fragment, boolean pIsAddToBackStack) {
//        getFragmentBackStack().switchFragment(fragment, pIsAddToBackStack);
//    }

//    public FragmentBackStack getFragmentBackStack() {
//        return mActivity.getFragmentBackStack();
//    }
}
