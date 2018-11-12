package cloud.cirrusup.mapper.example.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.google.common.base.MoreObjects;

/**
 * Model for borrows DynamoDB table.
 *
 * Primary key: borrowId
 * GSI:
 *  - gsi1: pk-> bookId
 *  - gsi2: pk-> userId, sk-> borrowTimestamp
 *  - gsi3: pk-> borrowStatus, sk-> borrowTimestamp
 *
 */
public class Borrows {

    private String borrowId;
    private String bookId;
    private String userId;
    private long borrowTimestamp;
    private String status;

    public Borrows() {
    }

    @DynamoDBHashKey(attributeName="borrowId")
    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "gsi1", attributeName = "bookId")
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "gsi2", attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexNames = "gsi2, gsi3", attributeName = "borrowTimestamp")
    public long getBorrowTimestamp() {
        return borrowTimestamp;
    }

    public void setBorrowTimestamp(long borrowTimestamp) {
        this.borrowTimestamp = borrowTimestamp;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "gsi3", attributeName = "borrowStatus")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("borrowId", borrowId)
                .add("bookId", bookId)
                .add("userId", userId)
                .add("borrowTimestamp", borrowTimestamp)
                .add("status", status)
                .toString();
    }


    public static class Builder {
        private String borrowId;
        private String bookId;
        private String userId;
        private long borrowTimestamp;
        private String status;

        public Builder() {
        }

        public Builder withBorrowId(String borrowId) {
            this.borrowId = borrowId;
            return this;
        }

        public Builder withBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withBorrowTimestamp(long borrowTimestamp) {
            this.borrowTimestamp = borrowTimestamp;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Borrows build() {
            Borrows borrows = new Borrows();

            borrows.setBorrowId(borrowId);
            borrows.setBookId(bookId);
            borrows.setStatus(status);
            borrows.setBorrowTimestamp(borrowTimestamp);
            borrows.setUserId(userId);

            return borrows;
        }
    }
}
