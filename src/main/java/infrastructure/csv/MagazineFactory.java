package infrastructure.csv;

import domain.model.Magazine;

/**
 * An implementation class of CSV factory responsible for creating {@link Magazine} objects from CSV row data.
 */
public class MagazineFactory implements CsvFactory<Magazine> {
    @Override
    public Magazine fromRow(String[] r) {
        String title     = r[0].trim();
        String publisher = r[1].trim();
        int year         = CsvUtils.parseInt(r[2], 0);
        var cats         = CsvUtils.splitCats(r[3]);
        return new Magazine(title, publisher, year, cats);
    }
}