package BetaBank;

import java.util.*;
import java.util.stream.Stream;


public class Bank {

    Scanner in = new Scanner(System.in);
    HashMap<String, Integer> customerData = new HashMap<>();
    ArrayList<BankClient> bankClients = new ArrayList<>();
    boolean isSuccessful = false;
    boolean exit = false;

    public void start() {

        System.out.println("Добро пожаловать в Beta Bank\nВставте карту\n1)Вставить\n2)Не вставлять");
        int cardInATM = in.nextInt();
        int amountOfAuthorizations = 0;


        while (amountOfAuthorizations < 3 && !exit)  {
            //проверка на вставку карты
            if (cardInATM == 1) {


                System.out.println("\nВведите логин и пароль");

                String tempLogin = in.next();
                int tempPassword = in.nextInt();


                //вызов метода авторизации
                authorization(tempPassword, tempLogin);

                //если авторизация прошла успешно и не успешных входов было меньше 3
                if (isSuccessful && amountOfAuthorizations < 3) {

                    //сброс авторизаций тк был выполнен успешный вход
                    amountOfAuthorizations = 0;

                    System.out.println("Добро пожаловать. Что вы хотите сделать\n");

                    //цикл работы банкомата
                    while (!exit) {
                        System.out.println(
                                "\n1) Посмотреть баланс\n" +
                                        "2) Пополнить счет\n" +
                                        "3) Снять деньги\n" +
                                        "4) Посмотреть историю транзакций\n" +
                                        "5) Завершить");
                        int action = in.nextInt();

                        if (action == 1) {
                            balance(tempLogin);
                        } else if (action == 2) {
                            refill(tempLogin);
                        } else if (action == 3) {
                            cashWithdrawal(tempLogin);
                        } else if (action == 4) {
                            transactions(tempLogin);
                        } else if (action == 5) {
                            exit = true;
                        }

                    }


                } else {
                    System.out.println("Неверный пин или превышено количество неверных авторизаций\n");
                    amountOfAuthorizations++;
                }

            } else if (cardInATM == 2) {
                System.out.println("Ну нет так нет");
                break;

            } else {
                System.out.println("Ошибка ввода");
            }
        }
    }

    //метод прохождения авторизации в банкомате
    private boolean authorization(Integer tempPassword, String tempLogin) {

        isSuccessful = customerData.entrySet().stream()
                .filter(client -> client.getKey().equals(tempLogin) && client.getValue().equals(tempPassword))
                .findFirst()
                .isPresent();

        return isSuccessful;
    }

    //метод вывода всех транзакций
    private void transactions(String userLogin) {
        System.out.println("Вот список ваших транзакций");

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent( client -> {
                    for (String list : client.clientTransactions) {
                        int count = 1;

                        System.out.println(client + ") " + list);
                        count++;
                    }
                });
    }

    //метод вывода баланса
    private void balance(String userLogin) {

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent(client -> System.out.println("Ваш баланс: " + client.getClientBalance()));
    }

    //метод пополнения
    private void refill(String userLogin) {

        System.out.println("Вставте все купюры в приемник");
        int replenishment = in.nextInt();

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent(client -> {
                    client.setClientBalance(client.getClientBalance() + replenishment);
                    client.clientTransactions.add("Пополнение счета на " + replenishment + "$");
                });

    }

    //метод снятия со счета
    private void cashWithdrawal(String userLogin) {

        System.out.println("Сколько вы хотите снять?");
        int withdrawal = in.nextInt();

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent(client -> {
                    if (client.getClientBalance() > withdrawal) {
                        client.setClientBalance(client.getClientBalance() - withdrawal);
                        client.clientTransactions.add("Снятие со счета " + withdrawal + "$");

                        bankClients.set(bankClients.indexOf(client), client);
                    } else {
                        System.out.println("На вашем счете меньше запрашивоемой суммы");
                    }
                });
    }

    //метод добовления нового клиента
    public void addBankClient() {
        System.out.println("Регистрация нового клиента\nПридумайте логин и пароль");

        String key = in.next();
        int value = in.nextInt();

        bankClients.add(new BankClient(0, key, value));
        customerData.put(key, value);
    }

}