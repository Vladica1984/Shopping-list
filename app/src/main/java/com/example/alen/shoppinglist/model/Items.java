package com.example.alen.shoppinglist.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Alen on 18-Apr-17.
 */

@DatabaseTable(tableName = Items.TABLE_NAME_ITEMS)
public class Items {

    public static final String TABLE_NAME_ITEMS = "items";
    public static final String TABLE_FIELD_ID = "id";
    public static final String TABLE_FIELD_NAME = "name";
    public static final String TABLE_FIELD_AMOUNT = "amount";
    public static final String TABLE_FIELD_MAINLIST = "main_list";

    @DatabaseField(columnName = TABLE_FIELD_ID, generatedId = true)
    private int idItems;
    @DatabaseField(columnName = TABLE_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = TABLE_FIELD_AMOUNT)
    private String amount;
    @DatabaseField(columnName = TABLE_FIELD_MAINLIST, foreign = true, foreignAutoRefresh = true)
    private MainList mainList;

    public Items() {
    }

    public int getIdItems() {
        return idItems;
    }

    public void setIdItems(int idItems) {
        this.idItems = idItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public MainList getMainList() {
        return mainList;
    }

    public void setMainList(MainList mainList) {
        this.mainList = mainList;
    }

    @Override
    public String toString() {
        return "Items{" +
                "idItems=" + idItems +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", mainList=" + mainList +
                '}';
    }
}
