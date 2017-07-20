package com.soulfly.umorili.dataBase.entityes;


import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.soulfly.umorili.dataBase.UmoriliDataBase;

import java.util.List;

@Table(database = UmoriliDataBase.class,
        orderedCursorLookUp = true,
        insertConflict = ConflictAction.ABORT)
public class AllJokesEntity extends BaseModel {

    @PrimaryKey
    private String id;

    @Column(name = "text")
    private String text;

    @Column(name = "favorites")
    private boolean favorites;

    @Column(name = "timestamp")
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static List<AllJokesEntity> selectAll() {
        return new Select()
                .from(AllJokesEntity.class)
                .queryList();
    }
    public static List<AllJokesEntity> selectAllSearch(String query) {
        return new Select()
                .from(AllJokesEntity.class)
                //.where("text LIKE?", new String[]{'%' + query + '%'})
                .queryList();
    }


    public static List<AllJokesEntity> selectFavorite() {

        return SQLite.select()
                .from(AllJokesEntity.class)
                .where(AllJokesEntity_Table.favorites.eq(true))
                .queryList();

    }

    public static void updateFavorites(@NonNull String quoteId) {
        SQLite.update(AllJokesEntity.class)
                .set(AllJokesEntity_Table.favorites.eq(true))
                .where(AllJokesEntity_Table.id.eq(quoteId))
                .execute();
    }

 public static void deleteFavorites(@NonNull String quoteId) {


     SQLite.delete(AllJokesEntity.class)
             .where(AllJokesEntity_Table.id.eq(quoteId))
             .async()
             .execute();
    }

    /*public static AllJokesEntity selectByLink(@NonNull String link) {
        AllJokesEntity allJokesEntity = SQLite.select()
                .from(AllJokesEntity.class)
                .where(AllJokesEntity_Table.id.eq(link)).querySingle();
        return allJokesEntity;
    }*/

}
