package com.soulfly.umorili.dataBase;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

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

    public static AllJokesEntity getInstance() {
        if(instance == null){
            instance = new AllJokesEntity();
        }
        return instance;
    }

    private static AllJokesEntity instance;

    public AllJokesEntity() {}

    public AllJokesEntity(String id, String text, boolean favorites, String timestamp) {
        this.id = id;
        this.text = text;
        this.favorites = favorites;
        this.timestamp = timestamp;
    }

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
                .orderBy(AllJokesEntity_Table.id, false)
                .queryList();
    }
    public static List<AllJokesEntity> selectAllSearch(String query) {
        if(!query.isEmpty()){
            return new Select()
                    .from(AllJokesEntity.class)
                    .where(AllJokesEntity_Table.text.like("%" + query + "%"))
                    .orderBy(AllJokesEntity_Table.id, false)
                    .queryList();
        } else{
            return new Select()
                    .from(AllJokesEntity.class)
                    .orderBy(AllJokesEntity_Table.id, false)
                    .queryList();
        }

    }


    public static List<AllJokesEntity> selectFavorite() {

        return SQLite.select()
                .from(AllJokesEntity.class)
                .where(AllJokesEntity_Table.favorites.eq(true))
                .queryList();

    }
}
