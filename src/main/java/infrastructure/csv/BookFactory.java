package infrastructure.csv;

import domain.model.Book;

/**
 * An implementation class of CSV factory responsible for creating {@link Book} objects from CSV row data.
 */
public class BookFactory implements CsvFactory<Book> {
    @Override
    public Book fromRow(String[] r) {
        String title  = r[0].trim();
        String author = r[1].trim();
        int year      = CsvUtils.parseInt(r[2], 0);
        var cats      = CsvUtils.splitCats(r[3]);
        return new Book(title, author, year, cats);
    }
}