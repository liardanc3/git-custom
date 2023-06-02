package gitp.gitcustom.shell.data;

import gitp.gitcustom.provider.GitDataProvider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Stores file path and committed message.
 * <p>Injected to {@link GitDataProvider}
 */
@Component
public class PathAndMessage extends LinkedHashMap<String, String> {

}