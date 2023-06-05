package gitp.gitcustom.shell.command;

import gitp.gitcustom.provider.GitDataProvider;

public interface Command {

    GitDataProvider gitDataProvider = null;

    void init();

    void afterTask();

}
