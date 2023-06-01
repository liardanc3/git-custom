package gitp.gitcustom.manager.data;

import lombok.Data;

/**
 * Stores committed date and file path.
 * <p>
 * Used in the priority queue of {@link gitp.gitcustom.manager.GitManager}
 */
@Data
public class DateAndPath implements Comparable<DateAndPath>{
    private Long date;
    private String filePath;

    @Override
    public int compareTo(DateAndPath o) {
        return Long.compare(this.date, o.date);
    }
}
