package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("");
        }
        if (accountDAO.isUsernameExists(username)) {
            throw new IllegalArgumentException("");
        }

        Account account = new Account(username, password);
        return accountDAO.registerAccount(account);
    }

    // Update Existing Account Password
    public boolean updatePassword(int account_id, String newPassword) {
        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("");
        }
        return accountDAO.updateAccountPassword(account_id, newPassword);
    }

    // Login Hanlder
    public Account login(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("");
        }

        Account account = accountDAO.getAccountForLogin(username, password);
        if (account == null) {
            throw new IllegalArgumentException("");
        }
        return account;
    }

}
