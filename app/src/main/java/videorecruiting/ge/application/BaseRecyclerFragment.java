package videorecruiting.ge.application;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import application.ibra.tryon.tryon.model.event.RecyclerViewScrollEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by thangtranquyet on 3/16/16.
 */
public class BaseRecyclerFragment extends BaseFragment {
    private final String Tag = BaseRecyclerFragment.class.getSimpleName();
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected LinearLayoutManager mLayoutManager;
    protected View mLayoutMessageWhenDataLengthIsZero;
    private boolean mRecyclerViewScrolled = false;
    private EventBus mEvenBus = EventBus.getDefault();

    private RecyclerView.OnScrollListener mScrollListenerForSwipeToRefreshLayout = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!mRecyclerViewScrolled) {
                mRecyclerViewScrolled = true;
            } else {
                mEvenBus.post(new RecyclerViewScrollEvent(dy));
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(mLayoutManager != null && mRefreshLayout != null){
                int firstPos = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(firstPos > 0){
                    if(mRefreshLayout != null)
                        mRefreshLayout.setEnabled(false);
                }
                else{
                    if(mRefreshLayout != null)
                        mRefreshLayout.setEnabled(true);
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerViewScrolled = false;
    }

    protected void fillDataToRecyclerView (){
        if(mRefreshLayout != null){
            mRefreshLayout.setRefreshing(false);
        }
    }

    protected void doRefreshAction (){

    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mScrollListenerForSwipeToRefreshLayout);
    }

    @Override
    protected void finishedView(boolean success) {
        super.finishedView(success);
        if(mRecyclerView != null){
            if(success){
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            else{
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
