package jufeng.juyancash.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15157_000 on 2016/6/8 0008.
 */
public class FilterArrayList<T> extends ArrayList<T>
{
    /**
     *
     */
    private static final long serialVersionUID = 1975316155027160540L;

    private IObjectFilter mFilter;
    private Integer[] mFilteredIndices; //过滤后的index,

    public FilterArrayList(List<T> list)
    {
        super(list);
    }

    public FilterArrayList()
    {
        super();
    }

    public boolean add(T object)
    {//如果当前list处于过滤状态,则不允许添加新的对象
        if(mFilter != null)
        {
            return false;
        }
        return super.add(object);
    }

    /*<br>　　 *  在过滤状态下,要处理一系列插入删除动作, 有待完善<br>　　 */

    @Override
    public int size()
    {
        if(mFilteredIndices != null)
        {//此时,过滤后的index集合的大小就是整个List的大小
            return mFilteredIndices.length;
        }
        else
        {
            return super.size();
        }
    }

    public T get(int index)
    {
        if(mFilteredIndices != null)
        {//过滤状态下,转换过滤后对应的index
            index = getFilteredIndex(index);
        }
        return super.get(index);
    }

    public void setFilter(IObjectFilter filter)
    {
        if(filter == null)
        {
            removeFilter();
        }
        else
        {
            mFilter = filter;
            applyFilter();
        }
    }

    private void applyFilter()
    {
        mFilteredIndices = null;
        int size = super.size();
        List<Integer> indices =new ArrayList<Integer>();
        for(int i=0;i<size;i++)
        {//调用过滤接口
            if(mFilter.filter(super.get(i)))
            {//如果返回true则保存此index
                indices.add(i);
            }
        }
        mFilteredIndices = new Integer[indices.size()];
        indices.toArray(mFilteredIndices);
    }

    public void removeFilter()
    {
        mFilter = null;
        mFilteredIndices = null;
    }

    private int getFilteredIndex(int index)
    {
        return mFilteredIndices[index];
    }
}