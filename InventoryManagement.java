package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

public class InventoryManagement {
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017/";
        try(MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("inventoryDB");
             collection =  mongoDatabase.getCollection("items");

            Scanner sc = new Scanner(System.in);

            while(true){
                System.out.println("Inventory Management System");
                System.out.println("1.Add items");
                System.out.println("2.Update quantity");
                System.out.println("3.Remove item");
                System.out.println("4.View inventory");
                System.out.println("5.Exit");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice){
                    case 1 :
                        addItems(sc);
                        break;
                    case 2 :
                        updateQuantity(sc);
                        break;
                    case 3 :
                        removeItem(sc);
                        break;
                    case 4 :
                        viewInventory();
                        break;
                    case 5 :
                        System.out.println("Exiting...");
                        return;

                    default :
                        System.out.println("Invalid option.Try again!");
                }
            }
        }
    }
public static void addItems(Scanner sc){
    System.out.println("Enter the item name: ");
    String name = sc.nextLine();
    System.out.println("Enter the quantity of item: ");
    int quantity = sc.nextInt();
    sc.nextLine();

    Document myDoc = new Document("item_name",name)
    .append("Quantity",quantity) ;

    collection.insertOne(myDoc);
    System.out.println("Item inserted successfully");
    System.out.println();
}

public static void updateQuantity(Scanner sc){
    System.out.println("Enter the name of the item: ");
    String name = sc.nextLine();
    System.out.println("Update the quantity of the item: ");
    int updateQnt = sc.nextInt();
    sc.nextLine();

    System.out.println("Debug: Updating item with name = " + name + " to quantity = " + updateQnt);


    long matchedCount = collection.updateOne(
            eq("item_name", name),
            new Document("$set", new Document("Quantity", updateQnt))
    ).getMatchedCount();

    if (matchedCount > 0) {
        System.out.println("Quantity updated successfully!");
    } else {
        System.out.println("No matching item found with the name: " + name);
    }
    System.out.println();
}

 public static void removeItem(Scanner sc){
     System.out.println("Enter the name of the item to be removed: ");
     String name = sc.nextLine();

     long matchedCount = collection.deleteOne(eq("item_name",name)).getDeletedCount();
     if (matchedCount > 0) {
         System.out.println(name+" has been removed!");
     } else {
         System.out.println("No matching item found with the name: " + name);
     }

     System.out.println();
 }

 public static void viewInventory(){
     for (Document item : collection.find()) {
         System.out.println("Item: " + item.getString("item_name") + ", Quantity: " + item.getInteger("Quantity"));
     }
     System.out.println();
 }
}

