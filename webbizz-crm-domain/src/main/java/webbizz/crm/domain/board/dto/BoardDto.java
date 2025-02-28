package webbizz.crm.domain.board.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.enumset.BoardConfigKey;
import webbizz.crm.domain.board.enumset.BoardType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BoardDto {

    private Long id;

    private BoardType type;

    private String name;

    private String description;

    private YN useDescriptionYn;

    private YN viewYn;

    private String createdId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private final Map<String, String> config = new HashMap<>();


    public boolean isUseConfig(final BoardConfigKey key) {
        return BoardConfigKey.isUseConfig(this.config, key);
    }

    public boolean hasPermission(final BoardConfigKey key, final UserDetails userDetails) {
        return this.hasPermission(key, userDetails != null ? userDetails.getAuthorities() : null);
    }

    public boolean hasPermission(final BoardConfigKey key, final Collection<? extends GrantedAuthority> authorities) {
        return BoardConfigKey.hasPermission(this.config, key, authorities);
    }

}
