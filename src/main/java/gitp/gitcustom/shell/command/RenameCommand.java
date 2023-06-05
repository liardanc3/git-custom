package gitp.gitcustom.shell.command;

import gitp.gitcustom.provider.GitDataProvider;
import gitp.gitcustom.provider.ShellProvider;
import gitp.gitcustom.shell.aop.annotation.ExceptionAspect;
import gitp.gitcustom.provider.data.DateAndPath;
import gitp.gitcustom.provider.data.PathAndCommit;
import gitp.gitcustom.shell.aop.exception.ArgumentException;
import lombok.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

@ShellComponent
@RequiredArgsConstructor
@Lazy
public class RenameCommand implements Command{

    private final GitDataProvider gitDataProvider;

    private Git git;
    private BufferedReader reader;
    private PriorityQueue<DateAndPath> dateAndPathPQ;
    private PathAndCommit PathAndCommits;
    private List<File> targetFiles;

    @PostConstruct
    @Override
    public void init(){
        git = gitDataProvider.getGit();
        reader = new BufferedReader(new InputStreamReader(System.in));
        dateAndPathPQ = gitDataProvider.getDateAndPathPQ();
        PathAndCommits = gitDataProvider.getPathAndCommit();
        targetFiles = new ArrayList<>();
    }

    @Override
    public void afterTask(){
        dateAndPathPQ.clear();
        PathAndCommits.clear();
        targetFiles.clear();
    }

    @ExceptionAspect
    @ShellMethod("rename")
    @SneakyThrows
    public void rename(@ShellOption(value = {"--file"}, arity = 2, defaultValue = "!n") String fileName[]) {

        Assert.isTrue(fileName.length == 2, () -> {throw new ArgumentException();});

        setPathWithDateAndCommit(new File("."), ".");
        findTargetFiles(new File("."), fileName);

        if(continueCheck()) {
            if(fileName.length == 2)
                renameFiles(fileName, new File("."),"");

            stageAndCommit(fileName);
            System.out.println("Commit complete and plz push manually. Or you can use the \'push\' command with enter an Access Token.");
        }
        afterTask();
    }

    @SneakyThrows
    private void setPathWithDateAndCommit(File file, String filePath) {
        if(!filePath.equals(".")){
            RevCommit commitLog = git
                    .log()
                    .add(git.getRepository().resolve(Constants.HEAD))
                    .addPath(filePath.substring(1))
                    .setMaxCount(1)
                    .call()
                    .iterator().next();

            if(commitLog != null) {
                PathAndCommits.put(file.getPath(), commitLog);
                dateAndPathPQ.add(new DateAndPath(commitLog.getCommitTime(), file.getPath()));
            }
        }

        // nested
        if (file.isDirectory()) {
            Optional.ofNullable(file.listFiles())
                    .ifPresent(childFiles -> Arrays.stream(childFiles)
                            .filter(childFile -> !childFile.getName().equals(".git"))
                            .forEach(childFile -> setPathWithDateAndCommit(childFile, filePath + childFile.getName() + "/")));
        }
    }

    private void findTargetFiles(File file, String[] fileName){
        if(file.getName().contains(fileName[0])){
            targetFiles.add(file);
        }

        if(file.isDirectory() && file.listFiles().length > 0) {
            for (File childFile : file.listFiles()) {
                findTargetFiles(childFile, fileName);
            }
        }
    }

    @SneakyThrows
    private void renameFiles(String[] fileName, File file, String path) {
        if(file.isDirectory()){
            for (File childFile : file.listFiles()) {
                renameFiles(fileName, childFile, path+file.getName()+"/");
            }
        }

        if(file.getName().contains(fileName[0])){
            Files.move(file.toPath(), new File(path + file.getName().replaceAll(fileName[0], fileName[1])).toPath());
        }
    }

    @SneakyThrows
    private boolean continueCheck(){
        System.out.println("\n  --- target list ----\n");
        for (File targetFile : targetFiles) {
            System.out.println(">> file : " + targetFile.getPath() + "\n" + PathAndCommits.get(targetFile.getPath()).getFullMessage());
        }
        if(targetFiles.isEmpty()){
            System.out.println("There is no target.");
            return false;
        }
        System.out.print("Would you like to continue? (Y/N) : ");
        return reader.readLine().toLowerCase().charAt(0) == 'y';
    }

    @SneakyThrows
    private void stageAndCommit(String[] fileName){
        while(!dateAndPathPQ.isEmpty()){
            String filePath = dateAndPathPQ
                    .poll()
                    .getFilePath();
            String message = PathAndCommits
                    .get(filePath)
                    .getFullMessage();

            System.out.println(filePath);

            File target = new File(filePath.replaceAll(fileName[0], fileName[1]));
            target.setLastModified(System.currentTimeMillis());

            git.add().addFilepattern(target.getPath()).call();
            git.commit().setMessage(message).call();
        }
    }
}
