package gitp.gitcustom.shell.command;

import gitp.gitcustom.provider.GitDataProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RenameCmdTest {

    @Autowired
    private GitDataProvider gitDataProvider;

    @Test
    void commitLogs() throws GitAPIException, IOException {
        Git git = gitDataProvider.getGit();

       RevCommit revCommit = git
                .log()
                .add(git.getRepository().resolve("HEAD"))
                .setMaxCount(1)
                .addPath("build.gradle")
                .call()
                .iterator().next();

        System.out.println("next.getFullMessage() = " + revCommit.getFullMessage());
    }
}