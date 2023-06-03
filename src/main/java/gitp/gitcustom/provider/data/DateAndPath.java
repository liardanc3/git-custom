package gitp.gitcustom.provider.data;

import gitp.gitcustom.provider.GitDataProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Stores committed date and file path.
 * <p>Used in the priority queue of {@link GitDataProvider}
 */
@Data
@AllArgsConstructor
public class DateAndPath implements Comparable<DateAndPath>{
    private Integer date;
    private String filePath;

    @Override
    public int compareTo(DateAndPath o) {
        return Integer.compare(this.date, o.date);
    }
}
