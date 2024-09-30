package kolya.study.bookservice.dto;


public class UserDto {
    private Long id;
    private String username;
    private String profileImage;
    private String email;
    private boolean activated;
    private String activationCode;

    public UserDto(Long id, String username, String profileImage, String email, boolean activated, String activationCode) {
        this.id = id;
        this.username = username;
        this.profileImage = profileImage;
        this.email = email;
        this.activated = activated;
        this.activationCode = activationCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return activated;
    }

    public void setActive() {
        activated = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
