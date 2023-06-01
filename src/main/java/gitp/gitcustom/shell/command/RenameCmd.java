package gitp.gitcustom.shell.command;

import gitp.gitcustom.manager.GitManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@ShellComponent
@RequiredArgsConstructor
@Lazy
public class RenameCmd {

    private final GitManager gitManager;

    @ShellMethod("rename")
    public void rename(@ShellOption(value = {"--file"}, arity = 2, defaultValue = "!n") String fileName[],
                       @ShellOption(value = {"--msg"}, arity = 2, defaultValue = "!n") String commitMsg[]) throws IOException {

        // TODO - NoArgsException
        if(fileName[0].equals("!n") && commitMsg[0].equals("!n")){

        }
        String branchName = gitManager.getGit().getRepository().getBranch();

        // file rename
        if(fileName.length == 2){

        }

        // message edit
        if(commitMsg.length == 2){

        }

    }


}
