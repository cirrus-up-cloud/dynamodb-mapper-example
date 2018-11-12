package cloud.cirrusup.mapper.example;

import cloud.cirrusup.mapper.example.datastore.BorrowsDataStore;
import cloud.cirrusup.mapper.example.datastore.BorrowsDataStoreDynamoDBMapper;
import cloud.cirrusup.mapper.example.model.Borrows;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import java.util.List;

/**
 * Entry point.
 */
public class TestDynamoDBMapper {

    private BorrowsDataStore dataStore;

    public static void main(String[] args) {

        TestDynamoDBMapper mapper = new TestDynamoDBMapper();
        mapper.runTest();

    }

    private void runTest() {

        //create here your dynamoDb client
        AmazonDynamoDB dynamoDB = null;

        DynamoDBMapperConfig.Builder builder = DynamoDBMapperConfig.builder();
        builder.setTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("borrows"));
        DynamoDBMapperConfig config = builder.build();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB, config);

        dataStore = new BorrowsDataStoreDynamoDBMapper(mapper);

        addItems();

        loadBorrowById();

        loadBorrowByBookId();

        loadAllBorrowsForUser();

        updateStatusForDelayedBooks();

        returnBook();

    }

    private void returnBook() {

        dataStore.returnBook("b2");
        System.out.println("\n\nLoad borrowed books by user [user1] after borrow [b2] is done: " + dataStore.getBorrowsForUser("user1"));

    }

    private void updateStatusForDelayedBooks() {

        List<Borrows> list = dataStore.getBorrowedBooksByStatusAndBeforeDate("borrowed", System.currentTimeMillis() - 14 * 24 * 3600 * 1000);
        for (Borrows borrow : list) {

            dataStore.updateStatus(borrow.getBorrowId(), "delayed");
        }

        list = dataStore.getBorrowedBooksByStatusAndBeforeDate("delayed", System.currentTimeMillis());
        System.out.println("\n\nList of delayed books: " + list);
    }

    private void loadAllBorrowsForUser() {

        System.out.println("\n\nLoad borrowed books by user [user1]: " + dataStore.getBorrowsForUser("user1"));
    }

    private void loadBorrowByBookId() {

        System.out.println("\n\nLoad borrow details for book [book2]: " + dataStore.getBorrowByBookId("book2"));
    }

    private void loadBorrowById() {

        System.out.println("Load borrow details for borrow [b3]: " + dataStore.getBorrowById("b3"));
    }

    private void addItems() {

        long now = System.currentTimeMillis();

        dataStore.addBorrow("b1", "book1", "user1", "borrowed", now - 24 * 3600 * 1000);
        dataStore.addBorrow("b2", "book2", "user1", "borrowed", now - 12 * 24 * 3600 * 1000);
        dataStore.addBorrow("b3", "book3", "user2", "borrowed", now - 20 * 24 * 3600 * 1000);
    }
}
