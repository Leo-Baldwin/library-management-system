package infrastructure.csv;

import domain.model.Member;

/**
 * An implementation class of CSV factory responsible for creating {@link Member} objects from CSV row data.
 */
public class MemberFactory implements CsvFactory<Member> {
    @Override
    public Member fromRow(String[] r) {
        String name  = r[0].trim();
        String email = r[1].trim();
        return new Member(name, email);
    }
}