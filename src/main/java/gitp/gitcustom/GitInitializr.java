package gitp.gitcustom;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GitInitializr {

    @Bean
    public Git git(){
        try {
            return Git.open(new File("."));
        } catch (IOException e) {
            System.out.println("It's not git directory");
            System.exit(0);
        }
        return null;
    }
}
