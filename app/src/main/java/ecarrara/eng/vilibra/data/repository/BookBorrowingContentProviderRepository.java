package ecarrara.eng.vilibra.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.data.mapper.BookBorrowingContentProviderMapper;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;

/**
 * Concrete implementation of {@link BookBorrowingRepository} that uses a ContentProvider
 * implementation to hold book borrowing data.
 *
 */
public class BookBorrowingContentProviderRepository implements BookBorrowingRepository {

    private ContentResolver contentResolver;
    private BookBorrowingContentProviderMapper bookBorrowingContentProviderMapper;

    public BookBorrowingContentProviderRepository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.bookBorrowingContentProviderMapper = new BookBorrowingContentProviderMapper();
    }


    @Override public List<BookBorrowing> borrowedBooks() {
        Cursor borrowedBooksCursor = contentResolver
                .query(VilibraContract.BORROWING_CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        List<BookBorrowing> borrowedBooks =
                this.bookBorrowingContentProviderMapper.transform(borrowedBooksCursor);
        borrowedBooksCursor.close();
        return borrowedBooks;
    }

    @Override public void addBookBorrowing(BookBorrowing bookBorrowing) {
        ContentValues bookBorrowingData =
                this.bookBorrowingContentProviderMapper.transform(bookBorrowing);
        Uri borrowingUri = contentResolver.insert(LendingEntry.CONTENT_URI, bookBorrowingData);
    }

    @Override public void removeBookBorrowing(BookBorrowing bookBorrowing) {
        final String[] selectionArgs =
                new String[]{
                        String.valueOf(bookBorrowing.getId())
                };

        final String deletionWhere = LendingEntry.COLUMN_LENDING_ID + " = ?";

        int rowsDeleted = contentResolver.delete(
                LendingEntry.CONTENT_URI,
                deletionWhere,
                selectionArgs);
    }

}
