package gitp.gitcustom.shell.command;


import gitp.gitcustom.provider.GitDataProvider;
import gitp.gitcustom.provider.ShellProvider;
import lombok.SneakyThrows;
import org.springframework.shell.standard.ShellComponent;

import javax.annotation.PostConstruct;

@ShellComponent
public interface Command {

    GitDataProvider gitDataProvider = null;
    ShellProvider shellProvider = null;

    @PostConstruct
    void init();

    void afterTask();

}
