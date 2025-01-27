package gitp.gitcustom.provider;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Data
public class ShellProvider implements PromptProvider {

    private final ApplicationContext applicationContext;

    /**
     * Set shell input form
     */
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("git-custom > ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }


    /**
     * Restricts access to the line 79 of
     * <p>{@link org.springframework.shell.result.ThrowableResultHandler}
     */
    @PostConstruct
    public void init() {
        CommandCatalog commandCatalog = applicationContext.getBean(CommandCatalog.class);
        commandCatalog.unregister("stacktrace");
    }
}
