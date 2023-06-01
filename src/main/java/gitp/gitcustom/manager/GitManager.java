package gitp.gitcustom.manager;

import gitp.gitcustom.manager.data.DateAndPath;
import gitp.gitcustom.manager.data.PathAndMessage;
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
public class GitManager {

    private Git git;
    private String branchName;

    /**
     * Store {@code DateAndPath} in ascending order based on the date.
     * <p>Used in {@link gitp.gitcustom.shell.command.RenameCmd}
     */
    private PriorityQueue<DateAndPath> dateAndPathPQ;

    /**
     * {@code LinkedHashMap} storing file paths and messages.
     * <p>Used in {@link gitp.gitcustom.shell.command.RenameCmd}
     */
    private PathAndMessage pathAndMessages;


    public GitManager(PathAndMessage pathAndMessages) throws IOException {
        this.git = Git.open(new File("."));
        this.branchName = git.getRepository().getBranch();

        this.dateAndPathPQ = new PriorityQueue<>();
        this.pathAndMessages = pathAndMessages;
    }
}
