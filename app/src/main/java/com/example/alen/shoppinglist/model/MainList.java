package com.example.alen.shoppinglist.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Alen on 18-Apr-17.
 */

@DatabaseTable(tableName = MainList.TABLE_NAME_MAINLIST)
public class MainList {

    public static final String TABLE_NAME_MAINLIST = "main_List";
    public static final String TABLE_FIELD_ID = "id";
    public static final String TABLE_FIELD_NAME = "name_of_list";
    public static final String TABLE_FIELD_COMPLETE = "complete";
    public static final String TABLE_FIELD_PROTECT = "protect";
    public static final String TABLE_FIELD_NOARTICLES = "no_of_articles";
    public static final String TABLE_FIELD_ITEM = "item";

    @DatabaseField(columnName = TABLE_FIELD_NAME)
    private String nameOfList;
    @DatabaseField(columnName = TABLE_FIELD_COMPLETE)
    private String complete;
    @DatabaseField(columnName = TABLE_FIELD_PROTECT)
    private String protect;
    @DatabaseField(columnName = TABLE_FIELD_NOARTICLES)
    private String noOfArticles;
    @DatabaseField(columnName = TABLE_FIELD_ID, generatedId = true)
    private int idMainList;
    @ForeignCollectionField(columnName = MainList.TABLE_FIELD_ITEM, eager = true)
    private ForeignCollection<Items> items;

    public MainList() {
    }

    public MainList(String nameOfList) {
        this.nameOfList = nameOfList;
    }

    public String getNameOfList() {
        return nameOfList;
    }

    public void setNameOfList(String nameOfList) {
        this.nameOfList = nameOfList;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getProtect() {
        return protect;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }

    public String getNoOfArticles() {
        return noOfArticles;
    }

    public void setNoOfArticles(String noOfArticles) {
        this.noOfArticles = noOfArticles;
    }

    public int getIdMainList() {
        return idMainList;
    }

    public void setIdMainList(int idMainList) {
        this.idMainList = idMainList;
    }

    public ForeignCollection<Items> getItems() {
        return items;
    }

    public void setItems(ForeignCollection<Items> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MainList{" +
                "nameofList='" + nameOfList + '\'' +
                ", complete='" + complete + '\'' +
                ", protect='" + protect + '\'' +
                ", idMainList=" + idMainList +
                '}';
    }
}
