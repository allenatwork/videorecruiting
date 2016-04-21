package videorecruiting.ge.application.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.ToggleButton;

/**
 * All utils for keyboard.
 */
public class KeyboardUtils {
    /**
     * Hide keyboard.
     *
     * @param activity activity
     */
    private static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null
                && activity.getCurrentFocus().getWindowToken() != null) {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
            activity.getCurrentFocus().clearFocus();
        }
    }

    /**
     * Show keyboard
     *
     * @param view view
     */
    public static void showSoftKeyboard(View view) {
        if (view.isFocused()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Hide keyboard when touch outside view.
     *
     * @param activity activity
     * @param view     view root
     */
    public static void hideSoftKeyboardViewGroup(final Activity activity, View view) {
        if (activity == null) {
            return;
        }
        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText) && !(view instanceof Button)) {
        if (!(view instanceof EditText) && !(view instanceof AdapterView) && !(view instanceof ScrollView) && !(view instanceof ToggleButton)) {
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(activity, v);
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            final int size = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < size; i++) {
                final View innerView = ((ViewGroup) view).getChildAt(i);
                hideSoftKeyboardViewGroup(activity, innerView);
            }
        }
    }


    public static void hideKeyboardOnLostFocus(final Activity activity, View v) {
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideSoftKeyboard(activity, v);
                }
            }
        });
    }

    /**
     * Show keyboard after a delay
     *
     * @param view      view
     * @param timeDelay time
     * @return {@link Handler}. Should call
     * {@link Handler#removeCallbacksAndMessages(Object)} with
     * parameter= null when view is destroyed to avoid memory leak.
     */
    public static Handler showDelayKeyboard(final View view, long timeDelay) {
        Handler handler = new Handler();
        if (view == null || view.getContext() == null) {
            return handler;
        }

        if (timeDelay < 0) {
            timeDelay = 0;
        }
        view.requestFocus();
        Runnable delayRunnable = new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        };
        handler.postDelayed(delayRunnable, timeDelay);
        return handler;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                final View focusView = activity.getCurrentFocus();
                if (focusView != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                    focusView.clearFocus();
                } else {
                    if (isKeyboardShow(activity)) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean isKeyboardShow(Activity activity) {
        // 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard
        // Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 100 + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
        final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

        final int heightDiff = activity.getWindow().getDecorView().getRootView().getHeight() - (r.bottom - r.top);
         /* Threshold size: dp to pixels, multiply with display density */
        return heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;
    }
//    Need call in getViewTreeObserver
//    public static boolean isKeyboardShown(View rootView) {
//    /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
//        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128 + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
//        final Rect r = new Rect();
//        rootView.getWindowVisibleDisplayFrame(r);
//        final DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
//        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
//        final int heightDiff = rootView.getBottom() - r.bottom;
//        /* Threshold size: dp to pixels, multiply with display density */
//        final boolean isKeyboardShown = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;
//        Log.d("check KeyboardShown", "isKeyboardShown ? " + isKeyboardShown + ", heightDiff:" + heightDiff + ", density:" + dm.density
//                + "root view height:" + rootView.getHeight() + ", rect:" + r);
//        return isKeyboardShown;
//    }
//
//    //http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
//    public final void setKeyboardListener(Activity activity/*, final OnKeyboardVisibilityListener listener*/) {
//        final View activityRootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            private boolean wasOpened;
//
//            private final int DefaultKeyboardDP = 100;
//
//            // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
//            private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
//
//            private final Rect r = new Rect();
//
//            @Override
//            public void onGlobalLayout() {
//                // Convert the dp to pixels.
//                final int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());
//
//                // Conclude whether the keyboard is shown or not.
//                activityRootView.getWindowVisibleDisplayFrame(r);
//                final int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
//                final boolean isShown = heightDiff >= estimatedKeyboardHeight;
//
//                if (isShown == wasOpened) {
//                    Log.d("Keyboard state", "Ignoring global layout change...");
//                    return;
//                }
//
//                wasOpened = isShown;
////                listener.onVisibilityChanged(isShown);
//            }
//        });
//    }

    /**
     * Must call restoreSoftInputModeFromManifest in destroy view
     * http://stackoverflow.com/questions/6138330/is-there-any-way-to-change-androidwindowsoftinputmode-value-from-java-class
     *
     * @param pFragment
     * @param mode
     */
    public static void setSoftInputModeCurrentFragment(Fragment pFragment, int mode) {
        pFragment.getActivity().getWindow().setSoftInputMode(mode);
    }

    public static void restoreSoftInputModeFromManifest(Fragment pFragment) {
        try {
            ActivityInfo activityInfo = pFragment.getActivity().getPackageManager().getActivityInfo(pFragment.getActivity().getComponentName(), 0);
            Log.e("ActivityInfo", activityInfo.toString());
            Log.e("ActivityInfo", "" + activityInfo.softInputMode);
            pFragment.getActivity().getWindow().setSoftInputMode(activityInfo.softInputMode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Throwable ignored) {
        }
    }
}
