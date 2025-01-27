package gitp.gitcustom.shell.command;

import gitp.gitcustom.provider.GitDataProvider;
import gitp.gitcustom.shell.aop.annotation.ExceptionAspect;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@ShellComponent
@RequiredArgsConstructor
@Lazy
public class PushCommand implements Command{

    private final GitDataProvider gitDataProvider;

    private Git git;
    private BufferedReader reader;

    @PostConstruct
    @Override
    public void init() {
        git = gitDataProvider.getGit();
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void afterTask() {
    }

    @ExceptionAspect
    @ShellMethod
    @SneakyThrows
    private void push() {
        System.out.print("Enter git user name : ");
        String userName = reader.readLine();

        System.out.print("Enter git password : ");
        String password = reader.readLine();

        CredentialsProvider credentialsProvider =
                new UsernamePasswordCredentialsProvider(userName, password);

        git.push().setCredentialsProvider(credentialsProvider).call();
    }
}
