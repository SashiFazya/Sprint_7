public class LoginData {
    private String login;
    private String password;

    public LoginData() {
    }

    public LoginData(Courier courier) {
        this.login = courier.getLogin();
        this.password = courier.getPassword();
    }

    public LoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
