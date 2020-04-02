import com.mongodb.MongoCommandException;
import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;


/**
 * @author Martin Dolinsky
 */
public class MongoCreateCollection {

	private static ArrayList docs;
	private static MongoCollection<Document> collection;

	public static void main(String[] args) {
		try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
			MongoDatabase database = mongoClient.getDatabase("company");

			try {
				database.createCollection("employees");
				System.out.println("Collection created successfully");
			} catch (MongoCommandException e) {
				database.getCollection("employees").drop();
			}

			docs = new ArrayList<Document>();
			collection = database.getCollection("employees");

			readEmployees();
			System.out.println("EMPTY");
			createEmployeeInsertOne(1,"Martin","Dolinsky");
			readEmployees();
			createEmployeesInsertMany();
			readEmployees();
			updateEmployee(4,"firstName","Updated");
			readEmployees();
			deleteEmployee(4);
			readEmployees();
		}
	}


	public static void createEmployeeInsertOne(int id, String firstName, String lastName) {

		Document d1 = new Document("_id", id);
		d1.append("firstName", firstName);
		d1.append("lastName", lastName);
		collection.insertOne(d1);
		System.out.println("\nInserted One");
	}

	public static void createEmployeesInsertMany() {
		Document d2 = new Document("_id", 2);
		d2.append("firstName", "Miroslav");
		d2.append("lastName", "Jackanin");
		docs.add(d2);

		Document d3 = new Document("_id", 3);
		d3.append("firstName", "Peter");
		d3.append("lastName", "Ganoczi");
		docs.add(d3);

		Document d4 = new Document("_id", 4);
		d4.append("firstName", "Jakub");
		d4.append("lastName", "Tomas");
		docs.add(d4);

		Document d5 = new Document("_id", 5);
		d5.append("firstName", "Jakub");
		d5.append("lastName", "Kutka");
		docs.add(d5);

		Document d6 = new Document("_id", 6);
		d6.append("firstName", "Patrik");
		d6.append("lastName", "Strausz");
		docs.add(d6);

		collection.insertMany(docs);
		System.out.println("\nInserted Many");
	}

	public static void readEmployees() {
		System.out.println("\nCollection Employees: ");
		try (MongoCursor< Document > cur = collection.find().iterator()) {
			while (cur.hasNext()) {
				Document doc = cur.next();
//				System.out.println(users.get(1) + ": " + users.get(2) + " " + users.get(3));
				System.out.println(doc.toJson());
			}
		}
	}

	public static void updateEmployee(int id,String column, String value) {
		collection.updateOne((Document) docs.get(id-2),
				new Document("$set", new Document(column, value)));
		System.out.println("\nUpdating " + column + " of employee " + id + " to " + value);
	}



	public static void deleteEmployee(int id) {
		System.out.println("\nDeleting employee " + id);
		collection.deleteOne(Filters.eq("_id", id));
		System.out.println("\nEmployee deleted successfully...");
	}
}
