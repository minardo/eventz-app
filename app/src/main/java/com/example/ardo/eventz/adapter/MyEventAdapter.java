package com.example.ardo.eventz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.ardo.eventz.R;
import com.example.ardo.eventz.activity.EventUpdateActivity;
import com.example.ardo.eventz.activity.MyEventActivity;
import com.example.ardo.eventz.model.MyEventModelResult;

import java.util.List;
import java.util.Random;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.MyEventHolder> {

    Context context;
    List<MyEventModelResult> myEventItemList;

    public String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

    public MyEventAdapter(Context context, List<MyEventModelResult> myEventItemList) {
        this.context = context;
        this.myEventItemList = myEventItemList;
    }

    @NonNull
    @Override
    public MyEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_event, parent, false);
        return new MyEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventHolder holder, int position) {
        final MyEventModelResult myeventitemlist = myEventItemList.get(position);
        holder.tvNameEvent.setText(myeventitemlist.getName());
        holder.tvDescEvent.setText(myeventitemlist.getDescription());


        Log.v("adapter "+position+" : ", myeventitemlist.getId().toString());
        String nameEvent = myeventitemlist.getName();
        String firstCharNameEvent = nameEvent.substring(0, 1);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstCharNameEvent, getColor());
        holder.ivTextDrawable.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return myEventItemList.size();
    }

    class MyEventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // This class for casting variable
        ImageView ivTextDrawable;
        TextView tvNameEvent, tvDescEvent;

        public MyEventHolder(View itemView) {
            super(itemView);

            ivTextDrawable = (ImageView) itemView.findViewById(R.id.ivTextDrawable);
            tvNameEvent = (TextView) itemView.findViewById(R.id.tvNameEvent);
            tvDescEvent = (TextView) itemView.findViewById(R.id.tvDescEvent);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EventUpdateActivity.class);
            i.putExtra("id", myEventItemList.get(getAdapterPosition()).getId());
            i.putExtra("name", myEventItemList.get(getAdapterPosition()).getName());
            i.putExtra("description", myEventItemList.get(getAdapterPosition()).getDescription());
            i.putExtra("place", myEventItemList.get(getAdapterPosition()).getPlace());
            i.putExtra("contact", myEventItemList.get(getAdapterPosition()).getContact());
            i.putExtra("quota", myEventItemList.get(getAdapterPosition()).getQuota());
            i.putExtra("time", myEventItemList.get(getAdapterPosition()).getTime());
            i.putExtra("event_type", myEventItemList.get(getAdapterPosition()).getEventType());


/*
            "id": 103,
                    "user_id": 49,
                    "name": "Django 101",
                    "description": "Introducing Django Web Framework",
                    "place": "Tangerangss",
                    "contact": "081911088626",
                    "quota": 50,
                    "time": "2018-07-02 11:00:00+00:00",
                    "event_type": "Workshop"*/
            context.startActivity(i);
        }
    }

    public int getColor() {
        String color;

        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];
        int colorAsInt = Color.parseColor(color);

        return colorAsInt;
    }
}
