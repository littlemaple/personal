package rainbow.com.rainbow.bean;

/**
 * Created by blue on 2015/7/7.
 */
public class AccountProxy {
    private static AccountProxy instance;

    private Account account;

    public static AccountProxy getInstance() {
        if (instance == null) {
            instance = new AccountProxy();
        }
        return instance;
    }

    public Account getCurrent() {
        if (account == null) {
            account = new Account();
            account.setUserName("18768177280");
            account.setPassword("123456");
        }
        return account;
    }

}
