package cloud.cirrusup.mapper.example.datastore;

import cloud.cirrusup.mapper.example.model.Borrows;

import java.util.List;

/**
 * Data store for borrows.
 */
public interface BorrowsDataStore {

    void addBorrow(String id, String bookId, String userId, String status, long borrowTimestamp);

    Borrows getBorrowById(String borrowId);

    Borrows getBorrowByBookId(String bookId);

    List<Borrows> getBorrowsForUser(String userId);

    List<Borrows> getBorrowedBooksByStatusAndBeforeDate(String status, long borrowTimestamp);

    void updateStatus(String id, String status);

    void returnBook(String id);
}
