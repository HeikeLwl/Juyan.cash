package jufeng.juyancash.ui.customview.WheeelView_dialog;

import android.content.Context;

import java.util.ArrayList;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {
    
    // items
    private ArrayList<T> items;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public ArrayWheelAdapter(Context context, ArrayList<T> items) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            ArrayList<T> item = items;
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.get(index).toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }
}
