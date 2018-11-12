package cloud.cirrusup.mapper.example.datastore;

import cloud.cirrusup.mapper.example.model.Borrows;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;

/**
 * Implementation of {@link BorrowsDataStore} using DynamoDB mapper.
 */
public class BorrowsDataStoreDynamoDBMapper implements BorrowsDataStore {

    private final DynamoDBMapper mapper;

    public BorrowsDataStoreDynamoDBMapper(DynamoDBMapper mapper) {

        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBorrow(String id, String bookId, String userId, String status, long borrowTimestamp) {

        mapper.save(new Borrows.Builder()
                .withBorrowId(id)
                .withBookId(bookId)
                .withUserId(userId)
                .withStatus(status)
                .withBorrowTimestamp(borrowTimestamp)
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Borrows getBorrowById(String borrowId) {
        return mapper.load(Borrows.class, borrowId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Borrows getBorrowByBookId(String bookId) {

        HashMap<String, AttributeValue> eav = Maps.newHashMap();
        eav.put(":v1", new AttributeValue().withS(bookId));

        DynamoDBQueryExpression<Borrows> queryExpression = new DynamoDBQueryExpression<Borrows>()
                .withIndexName("gsi1")
                .withConsistentRead(false)
                .withKeyConditionExpression("bookId = :v1")
                .withExpressionAttributeValues(eav);

        List<Borrows> list = mapper.query(Borrows.class, queryExpression);

        if (!list.isEmpty()) {

            return list.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<Borrows> getBorrowsForUser(String userId) {
        HashMap<String, AttributeValue> eav = Maps.newHashMap();
        eav.put(":v1", new AttributeValue().withS(userId));
        eav.put(":v2", new AttributeValue().withN(String.valueOf(0)));

        DynamoDBQueryExpression<Borrows> queryExpression = new DynamoDBQueryExpression<Borrows>()
                .withIndexName("gsi2")
                .withConsistentRead(false)
                .withKeyConditionExpression("userId = :v1 and borrowTimestamp >= :v2")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<Borrows> query = mapper.query(Borrows.class, queryExpression);
        List<Borrows> list = Lists.newArrayList();
        for (Borrows b : query) {
            list.add(b);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Borrows> getBorrowedBooksByStatusAndBeforeDate(String status, long borrowTimestamp) {

        HashMap<String, AttributeValue> eav = Maps.newHashMap();
        eav.put(":v1", new AttributeValue().withS(status));
        eav.put(":v2", new AttributeValue().withN(String.valueOf(borrowTimestamp)));

        DynamoDBQueryExpression<Borrows> queryExpression = new DynamoDBQueryExpression<Borrows>()
                .withIndexName("gsi3")
                .withConsistentRead(false)
                .withKeyConditionExpression("borrowStatus = :v1 and borrowTimestamp <= :v2")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<Borrows> query = mapper.query(Borrows.class, queryExpression);
        List<Borrows> list = Lists.newArrayList();
        for (Borrows b : query) {
            list.add(b);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatus(String id, String status) {

        Borrows item = getBorrowById(id);
        item.setStatus(status);

        mapper.save(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returnBook(String id) {

        Borrows item = getBorrowById(id);

        mapper.delete(item);
    }
}
