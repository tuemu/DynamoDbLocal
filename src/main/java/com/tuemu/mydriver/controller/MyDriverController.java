package com.tuemu.mydriver.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.tuemu.mydriver.dto.MusicItem;

@RestController    //WebAPIの機能を追加するアノテーション
public class MyDriverController {

	private static String accessKey = "AccessKey";
	private static String secretAccessKey = "SecretKey";
	private static AWSCredentials cre = new BasicAWSCredentials(accessKey, secretAccessKey);
	
    
    
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
			new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "local"))
			.build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    DynamoDBMapper mapper = new DynamoDBMapper(client);

    static String tableName = "ProductCatalog";
	
	@RequestMapping(value = "/test")    //URL「〜/test」にアクセスされたときのアノテーション
    public String test() {
    	//this.setupDynamoDb();
    	//client.setEndpoint("http://localhost:8000");
		this.musicMapperDemo();

        return "Hello World!";
    }
	
	private void musicMapperDemo() {
		
	       MusicItem keySchema = new MusicItem();
	        keySchema.setArtist("No One You Know");
	        keySchema.setSongTitle("Call Me Today");

	        try {
	            MusicItem result = mapper.load(keySchema);
	            if (result != null) {
	                System.out.println(
	                "The song was released in "+ result.getYear());
	            } else {
	                System.out.println("No matching song was found");
	            }
	        } catch (Exception e) {
	            System.err.println("Unable to retrieve data: ");
	            System.err.println(e.getMessage());
	        }
	}
    
    private void setupDynamoDb() {
    	client.setEndpoint("http://localhost:8000");
    	//client2.setEndpoint("http://localhost:8000");
        String tableName = "DynamoTest4";

		// テーブル一覧
		System.out.println("テーブル一覧: " + client.listTables());
		 
		// テーブル作成
		List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
		keySchema.add(new KeySchemaElement().withAttributeName("HogeId").withKeyType(KeyType.HASH));
		AttributeDefinition attrDef = new AttributeDefinition().withAttributeName("HogeId").withAttributeType(ScalarAttributeType.S);
		ProvisionedThroughput pt = new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(5L);
		System.out.println(client.createTable(new CreateTableRequest(tableName, keySchema).withAttributeDefinitions(attrDef).withProvisionedThroughput(pt)));

		// テーブル一覧
		System.out.println("テーブル一覧2: " + client.listTables());
    }

    private static void createItems() {

        Table table = dynamoDB.getTable(tableName);
        try {

            Item item = new Item().withPrimaryKey("Id", 120).withString("Title", "Book 120 Title")
                .withString("ISBN", "120-1111111111")
                .withStringSet("Authors", new HashSet<String>(Arrays.asList("Author12", "Author22")))
                .withNumber("Price", 20).withString("Dimensions", "8.5x11.0x.75").withNumber("PageCount", 500)
                .withBoolean("InPublication", false).withString("ProductCategory", "Book");
            table.putItem(item);

            item = new Item().withPrimaryKey("Id", 121).withString("Title", "Book 121 Title")
                .withString("ISBN", "121-1111111111")
                .withStringSet("Authors", new HashSet<String>(Arrays.asList("Author21", "Author 22")))
                .withNumber("Price", 20).withString("Dimensions", "8.5x11.0x.75").withNumber("PageCount", 500)
                .withBoolean("InPublication", true).withString("ProductCategory", "Book");
            table.putItem(item);

        }
        catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }
    
    

}
