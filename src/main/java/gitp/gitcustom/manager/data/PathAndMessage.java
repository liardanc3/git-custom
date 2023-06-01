package gitp.gitcustom.manager.data;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Stores file path and committed message.
 * <p>Injected to {@link gitp.gitcustom.manager.GitManager}
 */
@Component
public class PathAndMessage extends LinkedHashMap<String, String> {

}