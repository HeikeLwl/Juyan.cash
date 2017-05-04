package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现ViewPager页卡切换的适配器
 *
 * @author Administrator
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<RecyclerView> array;
    private int count = -1;

    public MyViewPagerAdapter(Context context, List<RecyclerView> array) {
        this.array = new ArrayList<>();
        this.array.addAll(array);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return array.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(array.get(arg1), 0);
        return array.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    public void updateData(List<RecyclerView> array1) {
        array.clear();
        array.addAll(array1);
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        count = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (count > -1) {
            count--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
