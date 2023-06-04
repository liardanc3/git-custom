package gitp.gitcustom.shell.command;

import gitp.gitcustom.provider.GitDataProvider;
import gitp.gitcustom.shell.aop.annotation.ExceptionAspect;
import gitp.gitcustom.provider.data.DateAndPath;
import gitp.gitcustom.provider.data.PathAndMessage;
import gitp.gitcustom.shell.aop.exception.ArgumentException;
import lombok.*;
import org.aspectj.apache.bcel.classfile.Constant;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@ShellComponent
@RequiredArgsConstructor
@Lazy
public class RenameCmd {

    private final GitDataProvider gitDataProvider;

    private Git git;
    private PriorityQueue<DateAndPath> dateAndPathPQ;
    private PathAndMessage pathAndMessages;
    private List<File> targetFiles;
    private BufferedReader bufferedReader;

    @PostConstruct
    void init(){
        git = gitDataProvider.getGit();
        dateAndPathPQ = gitDataProvider.getDateAndPathPQ();
        pathAndMessages = gitDataProvider.getPathAndMessages();
        targetFiles = new ArrayList<>();
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    void afterTask(){
        dateAndPathPQ.clear();
        pathAndMessages.clear();
        targetFiles.clear();
    }

    @ExceptionAspect
    @ShellMethod("rename")
    @SneakyThrows
    public void rename(@ShellOption(value = {"--file"}, arity = 2, defaultValue = "!n") String fileName[],
                       @ShellOption(value = {"--msg"}, arity = 2, defaultValue = "!n") String commitMsg[]) {

        Assert.isTrue(fileName.length == 2 || commitMsg.length == 2, () -> {throw new ArgumentException();});

        setPathWithDateAndMsg(new File("."), ".");
        findTargetFiles(new File("."), fileName, commitMsg, fileName.length == 2, commitMsg.length == 2);

        // show target
        if(continueCheck()) {
            if(fileName.length == 2)
                renameFiles(fileName, new File("."),"");
            stageAndCommit(fileName, commitMsg);
        }

        afterTask();
    }

    @SneakyThrows
    private void setPathWithDateAndMsg(File file, String filePath) {
        if(!filePath.equals(".")){
            RevCommit commitLog = git
                    .log()
                    .add(git.getRepository().resolve(Constants.HEAD))
                    .addPath(filePath.substring(1))
                    .setMaxCount(1)
                    .call()
                    .iterator().next();

            if(commitLog != null) {
                pathAndMessages.put(file.getPath(), commitLog.getFullMessage());
                dateAndPathPQ.add(new DateAndPath(commitLog.getCommitTime(), file.getPath()));
            }
        }

        // nested
        if (file.isDirectory()) {
            Optional.ofNullable(file.listFiles())
                    .ifPresent(childFiles -> Arrays.stream(childFiles)
                            .filter(childFile -> !childFile.getName().equals(".git"))
                            .forEach(childFile -> setPathWithDateAndMsg(childFile, filePath + childFile.getName() + "/")));
        }
    }

    private void findTargetFiles(File file, String[] fileName, String[] commitMsg, boolean findFiles, boolean findCommits){
        if(findFiles && file.getName().contains(fileName[0])){
            targetFiles.add(file);
        }
        if(findCommits && pathAndMessages.get(file.getPath()) != null && pathAndMessages.get(file.getPath()).contains(commitMsg[0])){
            targetFiles.add(file);
        }

        if(file.isDirectory() && file.listFiles().length > 0) {
            for (File childFile : file.listFiles()) {
                findTargetFiles(childFile, fileName, commitMsg, findFiles, findCommits);
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
            file.renameTo(new File(path + file.getName().replaceAll(fileName[0], fileName[1])));
        }
    }

    @SneakyThrows
    private boolean continueCheck(){
        System.out.println("- target list -");
        for (File targetFile : targetFiles) {
            System.out.println(">> file : " + targetFile.getPath() + "\n" + pathAndMessages.get(targetFile.getPath()) + "\n");
        }
        System.out.print("\nWould you like to continue? (Y/N) : ");
        return bufferedReader.readLine().toLowerCase().charAt(0) == 'y';
    }

    @SneakyThrows
    private void stageAndCommit(String[] fileName, String[] commitMsg){
        while(!dateAndPathPQ.isEmpty()){
            String filePath = dateAndPathPQ
                    .poll()
                    .getFilePath();
            String message = pathAndMessages
                    .get(fileName.length == 2 ? filePath.replaceAll(fileName[1], fileName[0]) : filePath)
                    .replaceAll(commitMsg.length == 2 ? commitMsg[0] : "", commitMsg.length == 2 ? commitMsg[1] : "");

            git.add().addFilepattern(filePath).call();
            git.commit().setMessage(message).call();
        }
    }
}
