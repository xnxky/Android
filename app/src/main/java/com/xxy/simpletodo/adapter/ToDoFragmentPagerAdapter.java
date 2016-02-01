package com.xxy.simpletodo.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xxy.simpletodo.R;
import com.xxy.simpletodo.fragment.TabFragment;

import java.util.List;

/**
 * Created by xxiao on 1/31/16.
 */
public class ToDoFragmentPagerAdapter
    extends FragmentPagerAdapter {
    private List<TabFragment>  fragmentList;
    private Context context;

    public ToDoFragmentPagerAdapter(
        FragmentManager fragmentManager,
        List<TabFragment> fragmentList,
        Context context
    ) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.context = context;
    }

    @Override
    public TabFragment getItem(int pos) {
        return fragmentList.get(pos);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public View getTabView(int pos){
        View view = LayoutInflater
            .from(context).
            inflate(R.layout.tab_view, null);
        ImageView img = (ImageView) view.findViewById(R.id.tab_image);
        img.setImageResource(fragmentList.get(pos).getTabImageId());
        return view;
    }

}
