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

/**
 * Hello world!
 *origin/Ethan-Test
 *
 */
public class App
{
    public static void main( String[] args )
    {
        int storeID = 2044; //Used to input store ID number
        Table selectedTable = null;
        DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
                new ProfileCredentialsProvider()));
        TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        Iterator<Table> iterator = tables.iterator();
        while (iterator.hasNext()) { //loops through all tables
            Table tempTable = iterator.next();
            System.out.println("Current table: " + tempTable.getTableName());
            if(tempTable.getTableName().equalsIgnoreCase(selectedStore)) //if current table=wanted table
            {
                selectedTable = tempTable;
                break;
            }
        }
        System.out.println("Selected table: " + selectedTable.getTableName());
        Item item = selectedTable.getItem("IDnum", 109);
        System.out.println( "Hello World!" );
    }
}
