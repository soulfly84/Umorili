package com.soulfly.umorili.ui.adapters;


import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soulfly.umorili.R;
import com.soulfly.umorili.dataBase.AllJokesEntity;
import com.soulfly.umorili.ui.fragments.FavoritesFragment;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteHolder> {


    private List<AllJokesEntity> favoriteList;
    private ClickListener clickListener;
    private OnClickRemove mOnClickRemove;

    private static final String TAG = "FavoritesAdapter";

    public FavoritesAdapter(List<AllJokesEntity> favoriteList, FavoritesFragment context) {
        this.favoriteList = favoriteList;

        try {
            mOnClickRemove = ((OnClickRemove) context);

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallbackPurchase.");
        }
    }

    @Override
    public FavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.favorites_item, parent, false);
        return new FavoriteHolder(itemView, clickListener);
    }


    @Override
    public void onBindViewHolder(final FavoriteHolder holder, final int position) {
        final AllJokesEntity favorite = favoriteList.get(position);
        holder.favoritesText.setText(Html.fromHtml(favorite.getText()));


        holder.favorite_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFav(position);
                AllJokesEntity.selectFavorite();
                holder.favorite_on.setVisibility(View.GONE);
                Log.d(TAG, "UpdateFavsTask, удаляю " + position);
                notifyItemRemoved(position);
            }
        });


    }


    private void removeFav(int position) {

        final AllJokesEntity listItem = favoriteList.get(position);
        if (favoriteList.get(position) != null) {
            favoriteList.get(position).delete();
            listItem.setFavorites(false);
            listItem.save();
            favoriteList.remove(position);
        }
        notifyItemRemoved(position);

        mOnClickRemove.showEmptyFavsCallback();

    }


    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteHolder extends RecyclerView.ViewHolder {

        TextView favoritesText;
        ImageView favorite_on;

        private ClickListener clickListener;

        public FavoriteHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            favoritesText = (TextView) itemView.findViewById(R.id.favoritesText);
            favorite_on = (ImageView) itemView.findViewById(R.id.favorite_on);
            this.clickListener = clickListener;
        }

    }
}

