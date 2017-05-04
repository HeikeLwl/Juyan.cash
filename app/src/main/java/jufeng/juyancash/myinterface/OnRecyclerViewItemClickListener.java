package jufeng.juyancash.myinterface;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public abstract class OnRecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerViewItemClickListener(RecyclerView view) {
        this.recyclerView = view;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    public abstract void onItemClick(RecyclerView.ViewHolder holder, int position);

    public void onLongPress(RecyclerView.ViewHolder holder, int position){}

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                int position = recyclerView.indexOfChild(child);
                onItemClick(recyclerView.getChildViewHolder(child), position);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                Log.d("###", "长按");
                int position = recyclerView.indexOfChild(child);
                OnRecyclerViewItemClickListener.this.onLongPress(recyclerView.getChildViewHolder(child), position);
            }
        }
    }
}
