package cs351.group5;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

public class App
{
    public static void main( String[] args )
    {
        //System.out.println(getItemInfo(2044, 1135)); //example code to pul item info based on storeid and itemid
        //newItem(2044, 2001, "Cards", 1, false, "Funny Card", 5, 10); //example code to add new item to table
    }

    public static Table getTable(int storeID)
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

    public static String getItemInfo(int storeID, int itemID)
    {
        Table selectedTable = getTable(storeID);
        Item currentItem = selectedTable.getItem("ItemID", itemID); //looks up item
        return currentItem.toJSONPretty(); //returns item info (json)
    }

    public static void newItem
            (
            int storeID, int itemID, String itemCategory, int itemExpiration,
                boolean itemExpires, String itemName, int itemPrice, int itemQuantity
            )
    {
        Table currentTable = getTable(storeID);
        Item newItem = new Item()
                .withPrimaryKey("ItemID", itemID)
                .withString("Category", itemCategory)
                .withInt("Expiration", itemExpiration)
                .withBoolean("Expires", itemExpires)
                .withString("Name", itemName)
                .withInt("Price", itemPrice)
                .withInt("Quantity", itemQuantity);
        currentTable.putItem(newItem);
    }
}
