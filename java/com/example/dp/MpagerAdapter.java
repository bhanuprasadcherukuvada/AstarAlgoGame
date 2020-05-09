package com.example.dp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MpagerAdapter extends PagerAdapter
{


    private int[] layouts;
    private LayoutInflater layoutInflater;
    private Context context;


    public MpagerAdapter(int[] layouts,Context context)
    {
        this.layouts=layouts;
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public Object instantiateItem(ViewGroup container,int position)
    {
        View view=layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container,int position,Object object)
    {
        View view=(View)object;
        container.removeView(view);
    }


}
