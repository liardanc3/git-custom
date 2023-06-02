package gitp.gitcustom.shell.command;

import gitp.gitcustom.provider.GitDataProvider;
import gitp.gitcustom.shell.aop.annotation.ExceptionAspect;
import gitp.gitcustom.shell.aop.exception.NoArgsException;
import gitp.gitcustom.shell.data.DateAndPath;
import gitp.gitcustom.shell.data.PathAndMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.PriorityQueue;

@ShellComponent
@RequiredArgsConstructor
@Lazy
public class RenameCmd {

    private final GitDataProvider gitDataProvider;

    private Git git;
    private PriorityQueue<DateAndPath> dateAndPathPQ;
    private PathAndMessage pathAndMessages;

    @PostConstruct
    void init(){
        git = gitDataProvider.getGit();
        dateAndPathPQ = gitDataProvider.getDateAndPathPQ();
        pathAndMessages = gitDataProvider.getPathAndMessages();
    }

    @ExceptionAspect
    @ShellMethod("rename")
    public void rename(@ShellOption(value = {"--file"}, arity = 2, defaultValue = "!n") String fileName[],
                       @ShellOption(value = {"--msg"}, arity = 2, defaultValue = "!n") String commitMsg[]) throws NoArgsException {

        // if no args
        if(fileName[0].equals("!n") && commitMsg[0].equals("!n")){
            throw new NoArgsException();
        }
        setPathWithDateAndMsg(new File("."));

        // file rename
        if(fileName.length == 2){
            renameFiles(new File("."), fileName);
        }

        // message edit
        if(commitMsg.length == 2){

        }

    }

    @SneakyThrows
    private void setPathWithDateAndMsg(File file) {

        // git log -n 1 --format="%cd" --date=iso -- <file-path>
        // git log -n 1 --pretty=%B -- <file-path>
        RevCommit commitLog = git
                .log()
                .addPath(file.getPath())
                .setMaxCount(1)
                .call()
                .iterator()
                .next();
        pathAndMessages.put(file.getPath(), commitLog.getFullMessage());
        dateAndPathPQ.add(new DateAndPath(commitLog.getCommitTime(), file.getPath()));

        // nested
        if (file.isDirectory()) {
            Optional.ofNullable(file.listFiles())
                    .ifPresent(childFiles -> Arrays.stream(childFiles)
                            .filter(childFile -> !childFile.getName().equals(".git"))
                            .forEach(childFile -> setPathWithDateAndMsg(childFile)));
        }
    }

    private void renameFiles(File file, String[] fileName) {
        if(file.getName().contains(".idea"))
            return;

        file.renameTo(new File(file.getPath().replaceAll(fileName[0], fileName[1])));

        if(file.isDirectory() && file.listFiles().length > 0) {
            for (File childFile : file.listFiles()) {
                renameFiles(childFile, fileName);
            }
        }
    }
}
