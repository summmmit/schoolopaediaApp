package com.jetnix.my.schoolopaediaapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * Created by summmmit on 8/2/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    private final Context context;
    private List<DrawerDataLayout> list = Collections.emptyList();
    private LayoutInflater layoutInflater;

    public DrawerAdapter(Context context, List<DrawerDataLayout> list){
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.drawer_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DrawerDataLayout drawerDataLayout = list.get(position);
        holder.title.setText(drawerDataLayout.rowTitle);
        holder.icon.setImageResource(drawerDataLayout.rowIconId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
