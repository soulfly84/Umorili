package com.soulfly.umorili.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.soulfly.umorili.R;
import com.soulfly.umorili.dataBase.AllJokesEntity;

import java.util.List;

public class AdapterAllJokes extends RecyclerView.Adapter<AdapterAllJokes.AllJokesHolder> {
    private static final String TAG = "AdapterAllJokes";
    private Context context;
    private List<AllJokesEntity> allJokesList;

    public AdapterAllJokes(Context context, List<AllJokesEntity> allJokesList) {
        this.context = context;
        this.allJokesList = allJokesList;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return allJokesList.size();
    }



    @Override
    public AllJokesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.all_jokes_item, parent, false);

        return new AllJokesHolder(itemView);
    }

    public AllJokesEntity getItemById(int itemId) {
        return allJokesList.get(itemId);
    }

    @Override
    public void onBindViewHolder(final AllJokesHolder holder, final int position) {
        final AllJokesEntity allJokesEntity = allJokesList.get(position);
        holder.text.setText(Html.fromHtml(allJokesEntity.getText()));

        if (allJokesEntity.isFavorites()) {
            holder.favorite.setImageResource(R.drawable.ic_star_on);
            Log.d(TAG, "В избранном " + position);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star_off);
            Log.d(TAG, "Не в избранном " + position);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllJokesEntity allJokesEntity = allJokesList.get(position);
                if (allJokesEntity.isFavorites()) {
                    allJokesEntity.setFavorites(false);
                    holder.favorite.setImageResource(R.drawable.ic_star_off);

                } else {
                    allJokesEntity.setFavorites(true);
                    holder.favorite.setImageResource(R.drawable.ic_star_on);

                }
                allJokesEntity.save();
            }
        });
    }

    public class AllJokesHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView favorite;
        ImageView favorite_on;
        CheckBox favorite_chb;

        public AllJokesHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.all_jokes_item_jokes_text);
            favorite = (ImageView) itemView.findViewById(R.id.favorite);
        }
    }
}

