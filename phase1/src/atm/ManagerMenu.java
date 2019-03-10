package atm;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import atm.db.BankDatabase;

import atm.model.AccountRequestModel;

public class ManagerMenu extends Menu {

    /** Displays options for the Bank Manager **/

    private Scanner userInput = new Scanner(System.in);
    BankDatabase centralDatabase;
    // BankManager manager = new BankManager();

    public ManagerMenu(BankDatabase database) {
        this.centralDatabase = database;
    }

    public void getOption() {

        int selection;

        System.out.println("\n Hello. Please select an option: \n");
        System.out.println("1 - Add New Client");
        System.out.println("2 - Undo Transaction");
        System.out.println("3 - Add An Account for a Client");
        System.out.println("4 - EXIT");

        selection = userInput.nextInt();

        switch (selection) {

            //TODO: finish writing case statements for BankManager options

            case 1:
                addClient();
                getOption(); // display options again after you're done with one option.
                break;
            case 2:
                undo();
                getOption();
                break;
            case 3:
                manageAccountRequests();
                break;
            case 4:
                running = false;
                break;
            default:
                System.out.println("ERROR. Please select an option from the list above.");
                getOption();
        }

    }

    private void addClient() {
        String username, firstName, lastName, password;
        userInput.nextLine();
        System.out.println("Enter a First Name: ");
        firstName = userInput.nextLine();
        System.out.println("Enter a Last Name: ");
        lastName = userInput.nextLine();
        System.out.println("Create a username: ");
        username = userInput.nextLine();
        System.out.println("Create a password: ");
        password = userInput.nextLine();
        if (centralDatabase.createUser(firstName, lastName, username, password))
            System.out.println("User: " + username + " Added Successfully.");

    }

    private void manageAccountRequests() {
        System.out.println("The following account requests are pending:");
        List<AccountRequestModel> accountRequestModelList = centralDatabase.getPendingAccountRequests();
        int idx = 0;
        for (AccountRequestModel accountRequestModel : accountRequestModelList) {
            System.out.println(idx + " - User: " + accountRequestModel.getRequesterUserId() + " Type: " + accountRequestModel.getRequestedAccountType().getName());
            idx++;
        }
        System.out.println((idx + 1) + " - Grant ALL");
        System.out.println((idx + 2) + " - Back");
        int requestIdx = -1;
        while (requestIdx != (idx + 2)) {
            System.out.println("Select an option:");
            requestIdx = userInput.nextInt();
            if (requestIdx == idx + 1) { // Grant all.
                for (AccountRequestModel accountRequestModel : accountRequestModelList) {
                    if (centralDatabase.grantAccount(accountRequestModel.getId()))
                        System.out.println("Granted user " + accountRequestModel.getRequesterUserId() + " access to a new " + accountRequestModel.getRequestedAccountType() + " account.");
                }
            } else if (requestIdx >= 0 && requestIdx <= idx) {
                AccountRequestModel accountRequestModel = accountRequestModelList.get(requestIdx);
                if (centralDatabase.grantAccount(accountRequestModel.getId()))
                    System.out.println("Granted user " + accountRequestModel.getRequesterUserId() + " access to a new " + accountRequestModel.getRequestedAccountType() + " account.");
            }
        }
    }

    public InputStreamReader openFile(String fileName) {
        return new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName));
    }

    private void undo() {
        //TODO: Needs to be completed.
    }

}
