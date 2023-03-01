package com.example.ims;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;

/*
Functions:
    getTable(int storeID)
        Returns the Table object for the storeID that was supplied.
    getItemInfo(int storeID, int itemID)
        Returns in json the attributes of the item searched for.
    getAllItems(int storeID)
        Returns the entire database in json for the specified storeID
    newItem(int storeID, int itemID, String itemCategory, int itemExpiration,boolean itemExpires, String itemName,
                                                                                  int itemPrice, int itemQuantity)
        Adds a new item to the inventory database of the storeID supplied.
    removeItem(int storeID, int itemID)
        Removes an item from the inventory database of the storeID supplied.
Notes:
    If you do newItem() to an itemID that already exists, it will overwrite the old values
    The removeItem() will not fail if the item being removed does not exist.
*/

public class App extends Application {
    public static Stage stage_primary; // application's primary stage

    @Override
    public void start(Stage stage_primary) throws IOException {
        this.stage_primary = stage_primary;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("home.fxml"));
        Scene scene = new Scene((Parent) fxmlLoader.load(), 850, 500);
        this.stage_primary.setTitle("Inventory Management System");
        this.stage_primary.setScene(scene);
        this.stage_primary.show();
    }
    public static void main( String[] args )
    {
        launch();
        //System.out.println(getItemInfo(2044, 1135)); //example code to pul item info based on storeid and itemid
        //System.out.println(getAllItems(2044));
        //newItem(2044, 2002, "Cards", 1, false, "Funny Card", 5, 10); //example code to add new item to table
        //removeItem(2044, 2001);
    }

    public static Table getTable(String storeID)
    {
        String tableName = "PartySuppliesStore" + storeID;
        Table selectedTable = null;
        DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
                new ProfileCredentialsProvider()));
        TableCollection<ListTablesResult> tables = dynamoDB.listTables(); //lists all tables
        Iterator<Table> iterator = tables.iterator();
        while (iterator.hasNext()) { //loops through all tables
            Table tempTable = iterator.next();
            if(tempTable.getTableName().equalsIgnoreCase(tableName)) //if current table=wanted table
            {
                selectedTable = tempTable;
                break;
            }
        }
        return selectedTable;
    }

    public static String getAllItems(String storeID)
    {
        String returnString = "";
        DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
                new ProfileCredentialsProvider()));
        Table selectedTable = getTable(storeID);
        Item currentItem = null; //looks up item
        ItemCollection<ScanOutcome> allItems = selectedTable.scan();
        Iterator<Item> allItemsIterator = allItems.iterator();
        while (allItemsIterator.hasNext())
        {
            currentItem = allItemsIterator.next();
            returnString += currentItem.toJSONPretty();
        }
        return returnString; //returns item info (json)
    }

    public static String getItemInfo(String storeID, int itemID)
    {
        Table selectedTable = getTable(storeID);
        Item currentItem = selectedTable.getItem("ItemID", itemID); //looks up item
        return currentItem.toJSONPretty(); //returns item info (json)
    }

    public static void newItem
            (
                    String storeID, int itemID, String itemCategory, String itemExpiration,
                    boolean itemExpires, String itemName, int itemPrice, int itemQuantity
            )
    {
        Table currentTable = getTable(storeID);
        Item newItem = new Item()
                .withPrimaryKey("ItemID", itemID)
                .withString("Category", itemCategory)
                .withString("Expiration", itemExpiration)
                .withBoolean("Expires", itemExpires)
                .withString("Name", itemName)
                .withInt("Price", itemPrice)
                .withInt("Quantity", itemQuantity);
        currentTable.putItem(newItem);
    }
    public static void removeItem(String storeID, int itemID)
    {
        Table currentTable = getTable(storeID);
        DeleteItemOutcome removedItem = currentTable.deleteItem("ItemID", itemID);
    }

}