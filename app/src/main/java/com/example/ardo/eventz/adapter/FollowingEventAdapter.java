package com.example.ardo.eventz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.ardo.eventz.R;
import com.example.ardo.eventz.model.FollowingEventModelResult;

import java.util.List;
import java.util.Random;

public class FollowingEventAdapter extends RecyclerView.Adapter<FollowingEventAdapter.FollowingEventHolder> {

    Context context;
    List<FollowingEventModelResult> followingEventItemList;

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

    public FollowingEventAdapter(Context context, List<FollowingEventModelResult> followingEventItemList) {
        this.context = context;
        this.followingEventItemList = followingEventItemList;
    }

    @NonNull
    @Override
    public FollowingEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following_event, parent, false);
        return new FollowingEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingEventHolder holder, int position) {
        final FollowingEventModelResult followingeventitemlist = followingEventItemList.get(position);
        holder.tvNameEvent.setText(followingeventitemlist.getName());
        holder.tvDescEvent.setText(followingeventitemlist.getDescription());

        String nameEvent = followingeventitemlist.getName();
        String firstCharNameEvent = nameEvent.substring(0, 1);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstCharNameEvent, getColor());
        holder.ivTextDrawable.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return followingEventItemList.size();
    }

    class FollowingEventHolder extends RecyclerView.ViewHolder {

        // This class for casting variable
        ImageView ivTextDrawable;
        TextView tvNameEvent, tvDescEvent;

        public FollowingEventHolder(View itemView) {
            super(itemView);

            ivTextDrawable = (ImageView) itemView.findViewById(R.id.ivTextDrawable);
            tvNameEvent = (TextView) itemView.findViewById(R.id.tvNameEvent);
            tvDescEvent = (TextView) itemView.findViewById(R.id.tvDescEvent);
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
