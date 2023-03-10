package com.example.ims;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

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
    private static DynamoDbClient ddb; // service client for accessing DynamoDB

    @Override
    public void start(Stage stage) throws IOException {
        stage_primary = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("home.fxml"));
        Scene scene = new Scene((Parent) fxmlLoader.load(), 850, 500);
        stage_primary.setTitle("Inventory Management System");
        stage_primary.setScene(scene);
        stage_primary.show();
    }
    
    public static void main( String[] args )
    {
        // Establish connection with DynamoDB
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        ddb = DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();

        launch(); // <--- comment/uncomment to toggle GUI launch


        /*--------------------------------------
         * Example SDK Function Calls
         * --------------------------------------*/

        // Get item and print its attributes
        Map<String,AttributeValue> item = getItem("TestTable", "0002");
        if (item != null) {
            Set<String> keys = item.keySet();
            //System.out.println("Amazon DynamoDB table attributes: \n");


            for (String key1 : keys) {
                System.out.format("%s: %s\n", key1, item.get(key1).toString());
            }
        } else {
            System.out.format("No item found!\n");
        }



        // Print all tables
        //System.out.println("\nAll available tables:");
        for (String curName : listAllTables()) {
            System.out.format("* %s\n", curName);
        }

       // scanItems("PartySuppliesStore2044");
        // Add a new item to the table
/*        String[] new_item = {"0003", "Milk", "Consumable", "3.99", "12", "03/17/23"};
        addItem("TestTable", new_item);*/

        // Close the client
        ddb.close();
    }

    public static Map<String, AttributeValue> getItem(String tableName, String itemID ) {

        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("ItemID", AttributeValue.builder()
                .s(itemID)
                .build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();
            return returnedItem;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

   public static void addItem(String tableName, String[] values){
        String[] column_names = {"ItemID", "Name", "Category",  "Price", "Quantity", "Expiration"};

        // Add all values to hashmap of attributes
        HashMap<String,AttributeValue> itemValues = new HashMap<>();
        for(int i = 0; i < values.length; i++){
            itemValues.put(column_names[i], AttributeValue.builder().s(values[i]).build());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            PutItemResponse response = ddb.putItem(request);
            System.out.println(tableName +" was successfully updated. The request id is "+response.responseMetadata().requestId());

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    public static Map<String, AttributeValue> getItem(String tableName, String itemID ) {

        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("ItemID", AttributeValue.builder()
                .s(itemID)
                .build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();
            return returnedItem;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static List<String> listAllTables(){

        boolean moreTables = true;
        String lastName = null;
        List<String> tableNames = new ArrayList<>();

        while(moreTables) {
            try {
                ListTablesResponse response = null;
                if (lastName == null) {
                    ListTablesRequest request = ListTablesRequest.builder().build();
                    response = ddb.listTables(request);
                } else {
                    ListTablesRequest request = ListTablesRequest.builder()
                            .exclusiveStartTableName(lastName).build();
                    response = ddb.listTables(request);
                }

                tableNames = response.tableNames();
                if (tableNames.size() > 0) {
                    return tableNames;
                } else {
                    System.out.println("No tables found!");
                    System.exit(0);
                }

                lastName = response.lastEvaluatedTableName();
                if (lastName == null) {
                    moreTables = false;
                }

            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        return tableNames;
    }

    
    public static String[][] scanItems(String tableName ) {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        ddb = DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();
        String[][] allValues = new String[500][500];
        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();
            int dbRow = 0;
            int dbColum = 0;
            ScanResponse response = ddb.scan(scanRequest);
            for (Map<String, AttributeValue> item : response.items()) {
                Set<String> keys = item.keySet();
                for (String key : keys) {
                    String dbJunkValue = String.valueOf(item.get(key));
                    Scanner removeExtra = new Scanner(dbJunkValue);
                    removeExtra.useDelimiter("=");
                    removeExtra.next();
                    String dbSemiValue = removeExtra.next();
                    String dbValue = dbSemiValue.substring(0, dbSemiValue.length() - 1);
                    allValues[dbRow][dbColum] = dbValue;
                    dbColum++;
                    if(dbColum >= 6) {
                        dbColum = 0;
                        dbRow++;
                    }
                }
            }

        } catch (DynamoDbException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return allValues;
    }

    public static void deleteItem(String tableName, String itemID) {
        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("ItemID", AttributeValue.builder()
                .s(itemID)
                .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .build();

        try {
            ddb.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}