package gitp.gitcustom.provider;

import gitp.gitcustom.provider.data.DateAndPath;
import gitp.gitcustom.provider.data.PathAndCommit;
import gitp.gitcustom.shell.command.RenameCommand;
import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.springframework.shell.standard.ShellComponent;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 * GitManager provides git connection and data.
 * <p>Injected to classes in {@link gitp.gitcustom.shell.command}
 */
@ShellComponent
@Data
public class GitDataProvider {

    private Git git;

    /**
     * Store {@code DateAndPath} in ascending order based on the date.
     * <p>Used in {@link RenameCommand}
     */
    private PriorityQueue<DateAndPath> dateAndPathPQ;

    /**
     * {@code LinkedHashMap} storing file paths and messages.
     * <p>Used in {@link RenameCommand}
     */
    private PathAndCommit PathAndCommits;


    public GitDataProvider(PathAndCommit PathAndCommits) throws IOException {
        this.git = Git.open(new File("."));

        this.dateAndPathPQ = new PriorityQueue<>();
        this.PathAndCommits = PathAndCommits;
    }
}
