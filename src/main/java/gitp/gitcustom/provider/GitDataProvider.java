package gitp.gitcustom.provider;

import gitp.gitcustom.provider.data.DateAndPath;
import gitp.gitcustom.provider.data.PathAndCommit;
import gitp.gitcustom.shell.command.RenameCommand;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 * GitManager provides git connection and data.
 * <p>Injected to classes in {@link gitp.gitcustom.shell.command}
 */
@Component
@RequiredArgsConstructor
@Data
public class GitDataProvider {

    /**
     * {@code LinkedHashMap} storing file paths and messages.
     * <p>Used in {@link RenameCommand}
     */
    private final PathAndCommit pathAndCommit;

    /**
     * Store {@code DateAndPath} in ascending order based on the date.
     * <p>Used in {@link RenameCommand}
     */
    private PriorityQueue<DateAndPath> dateAndPathPQ;

    private Git git;

    @PostConstruct
    @SneakyThrows
    void init(){
        this.git = Git.open(new File("."));
        this.dateAndPathPQ = new PriorityQueue<>();
    }
}
