package BetaBank;

import java.util.*;
import java.util.stream.Stream;


public class Bank {

    Scanner in = new Scanner(System.in);
    private HashMap<String, Integer> customerData = new HashMap<>();
    private ArrayList<BankClient> bankClients = new ArrayList<>();
    private boolean isSuccessful = false;
    private boolean exit = false;
    private boolean signOut = false;

    public void start() {

        while (!exit){

            signOut = false;
            System.out.println("Добро пожаловать в Beta Bank, у вас уже счет?\n1)Да\n2)Нет или хочу открыть новый");
            int action = in.nextInt();


            if (action == 1){

                //ведет посчет авторизаций, если больше 3 то банкомат отключается
                int amountOfAuthorizations = 0;

                while (amountOfAuthorizations < 3 && !signOut)  {
                    System.out.println("\nВведите логин и пароль из цифр");

                    String tempLogin = in.next();
                    int tempPassword = in.nextInt();

                    //вызов метода авторизации
                    authorization(tempPassword, tempLogin);

                    //если авторизация прошла успешно и не успешных входов было меньше 3
                    if (isSuccessful && amountOfAuthorizations < 3) {
                        //сброс авторизаций тк был выполнен успешный вход
                        amountOfAuthorizations = 0;

                        System.out.println("Добро пожаловать. Что вы хотите сделать\n");

                        //region основной цикл работы банкомата
                        while (!signOut) {

                            System.out.println(
                                    "1) Посмотреть баланс\n" +
                                            "2) Пополнить счет\n" +
                                            "3) Снять деньги\n" +
                                            "4) Посмотреть историю транзакций\n" +
                                            "5) Вынуть карту\n" + //возможность зайти под другим логином
                                            "6) Завершить работу"); //заверщение работы приложения
                            action = in.nextInt();

                            if (action == 1) {
                                balance(tempLogin);
                            } else if (action == 2) {
                                refill(tempLogin);
                            } else if (action == 3) {
                                cashWithdrawal(tempLogin);
                            } else if (action == 4) {
                                transactions(tempLogin);
                            } else if (action == 5) {
                                signOut = true;
                            } else if (action == 6) {
                                signOut = true;
                                exit = true;
                            }

                        }
                        //endregion

                    } else {
                        System.err.println("Неверный пин \n");
                        amountOfAuthorizations++;

                        if(amountOfAuthorizations == 3) {
                            System.err.println("Превышено количество неверных авторизаций\n");
                            signOut = true;
                            exit = true;
                        }
                    }
                }

            } else if (action == 2) {
                addBankClient();
            }else {
                System.err.println("Некорректный ввод");
            }

        }
    }

    //метод прохождения авторизации в банкомате
    private boolean authorization(Integer tempPassword, String tempLogin) {

        isSuccessful = customerData.entrySet().stream()
                .anyMatch(client -> client.getKey().equals(tempLogin) && client.getValue().equals(tempPassword));
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
    private void addBankClient() {
        System.out.println("Регистрация нового клиента\nПридумайте логин и пароль из цифр");

        String key = in.next();
        int value = in.nextInt();

        bankClients.add(new BankClient(0, key, value));
        customerData.put(key, value);

        System.out.println("Спасибо за регистрацию, возвращение на главный экран\n");
    }

}