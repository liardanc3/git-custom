package gitp.gitcustom.provider.data;

import gitp.gitcustom.provider.GitDataProvider;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Stores file path and RevCommit.
 * <p>Injected to {@link GitDataProvider}
 */
@Component
public class PathAndCommit extends LinkedHashMap<String, RevCommit> {

}