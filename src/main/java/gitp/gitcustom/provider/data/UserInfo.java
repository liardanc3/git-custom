package gitp.gitcustom.provider.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class UserInfo {

    private String userName;
    private String password;
}
