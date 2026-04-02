package infrastructure.csv;

import domain.model.Dvd;

/**
 * An implementation class of CSV factory responsible for creating {@link Dvd} objects from CSV row data.
 */
public class DvdFactory implements CsvFactory<Dvd> {
    @Override
    public Dvd fromRow(String[] r) {
        String title  = r[0].trim();
        int year      = CsvUtils.parseInt(r[1], 0);
        int duration  = CsvUtils.parseInt(r[2], 0);
        String rating = r[3].trim();
        var cats      = CsvUtils.splitCats(r[4]);
        return new Dvd(title, year, duration, rating, cats);
    }
}