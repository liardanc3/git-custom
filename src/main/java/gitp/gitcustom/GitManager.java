package gitp.gitcustom;

import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.springframework.shell.standard.ShellComponent;

import java.io.File;
import java.io.IOException;

/**
 * GitManager provides git connection and branch name.
 * <br>Ensuring singleton and DI
 */
@ShellComponent
@Data
public class GitManager {

    private Git git;
    private String branchName;

    public GitManager() throws IOException {
        this.git = Git.open(new File("."));
        this.branchName = git.getRepository().getBranch();
    }
}
