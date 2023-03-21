package BetaBank;

import java.util.ArrayList;
import java.util.HashMap;

public class BankClient {
    private int clientBalance;
    private String login;
    private int password;

    ArrayList<String> clientTransactions = new ArrayList<>();

    public BankClient(int clientBalance, String login, int password) {
        this.clientBalance = clientBalance;
        this.login = login;
        this.password = password;
    }

    public int getClientBalance() {
        return clientBalance;
    }

    public void setClientBalance(int clientBalance) {
        this.clientBalance = clientBalance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
