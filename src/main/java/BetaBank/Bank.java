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

        System.out.println("����� ���������� � Beta Bank\n������� �����\n1)��������\n2)�� ���������");
        int cardInATM = in.nextInt();
        int amountOfAuthorizations = 0;


        while (amountOfAuthorizations < 3 && !exit)  {
            //�������� �� ������� �����
            if (cardInATM == 1) {


                System.out.println("\n������� ����� � ������");

                String tempLogin = in.next();
                int tempPassword = in.nextInt();


                //����� ������ �����������
                authorization(tempPassword, tempLogin);

                //���� ����������� ������ ������� � �� �������� ������ ���� ������ 3
                if (isSuccessful && amountOfAuthorizations < 3) {

                    //����� ����������� �� ��� �������� �������� ����
                    amountOfAuthorizations = 0;

                    System.out.println("����� ����������. ��� �� ������ �������\n");

                    //���� ������ ���������
                    while (!exit) {
                        System.out.println(
                                "\n1) ���������� ������\n" +
                                        "2) ��������� ����\n" +
                                        "3) ����� ������\n" +
                                        "4) ���������� ������� ����������\n" +
                                        "5) ���������");
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
                    System.out.println("�������� ��� ��� ��������� ���������� �������� �����������\n");
                    amountOfAuthorizations++;
                }

            } else if (cardInATM == 2) {
                System.out.println("�� ��� ��� ���");
                break;

            } else {
                System.out.println("������ �����");
            }
        }
    }

    //����� ����������� ����������� � ���������
    private boolean authorization(Integer tempPassword, String tempLogin) {

        isSuccessful = customerData.entrySet().stream()
                .filter(client -> client.getKey().equals(tempLogin) && client.getValue().equals(tempPassword))
                .findFirst()
                .isPresent();

        return isSuccessful;
    }

    //����� ������ ���� ����������
    private void transactions(String userLogin) {
        System.out.println("��� ������ ����� ����������");

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

    //����� ������ �������
    private void balance(String userLogin) {

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent(client -> System.out.println("��� ������: " + client.getClientBalance()));
    }

    //����� ����������
    private void refill(String userLogin) {

        System.out.println("������� ��� ������ � ��������");
        int replenishment = in.nextInt();

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent(client -> {
                    client.setClientBalance(client.getClientBalance() + replenishment);
                    client.clientTransactions.add("���������� ����� �� " + replenishment + "$");
                });

    }

    //����� ������ �� �����
    private void cashWithdrawal(String userLogin) {

        System.out.println("������� �� ������ �����?");
        int withdrawal = in.nextInt();

        bankClients.stream()
                .filter(client -> client.getLogin().equals(userLogin))
                .findFirst()
                .ifPresent(client -> {
                    if (client.getClientBalance() > withdrawal) {
                        client.setClientBalance(client.getClientBalance() - withdrawal);
                        client.clientTransactions.add("������ �� ����� " + withdrawal + "$");

                        bankClients.set(bankClients.indexOf(client), client);
                    } else {
                        System.out.println("�� ����� ����� ������ ������������� �����");
                    }
                });
    }

    //����� ���������� ������ �������
    public void addBankClient() {
        System.out.println("����������� ������ �������\n���������� ����� � ������");

        String key = in.next();
        int value = in.nextInt();

        bankClients.add(new BankClient(0, key, value));
        customerData.put(key, value);
    }

}