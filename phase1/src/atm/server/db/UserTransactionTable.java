package atm.server.db;

import atm.model.AccountModel;
import atm.model.TransactionModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class UserTransactionTable {
    public HashMap<Long, HashSet<TransactionModel>> transactionsForUserId = new HashMap<>();
    private long nextTransactionId = 0;

    public void save(String fileName) {
        try {
            PrintWriter writer = Util.openFileW(fileName);
            writer.println("id,userId,srcAccId,destAccId,amount");
            for (Map.Entry<Long, HashSet<TransactionModel>> userAccountsEntry : transactionsForUserId.entrySet()) {
                for (TransactionModel transactionModel : userAccountsEntry.getValue())
                    writer.println(transactionModel.toCSVRowString());
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to write CSV: " + fileName + ".");
        }
    }

    public void load(String fileName) {
        this.transactionsForUserId.clear();
        this.nextTransactionId = 0;
        Util.loadCSV(fileName, row -> addTransactionModel(TransactionModel.fromCSVRowString(row)));
    }

    public void addTransactionModel(TransactionModel transactionModel) {
        if (transactionModel.getId() > nextTransactionId) nextTransactionId = transactionModel.getId() + 1;
        createEntryForUser(transactionModel.getUserId(), transactionModel);
    }

    public void createEntryForUser(long userId, TransactionModel transactionModel) {
        HashSet<TransactionModel> userTransactionList;
        if ((userTransactionList = transactionsForUserId.get(userId)) != null) {
            userTransactionList.add(transactionModel);
        } else {
            userTransactionList = new HashSet<>();
            userTransactionList.add(transactionModel);
            transactionsForUserId.put(userId, userTransactionList);
        }
    }

    public void createTransactionModel(long userId, long srcAccId, long destAccId, double amount) {
        addTransactionModel(new TransactionModel(nextTransactionId, userId, srcAccId, destAccId, amount));
        nextTransactionId++;
    }

//    public TransactionModel getTransactionModelForId(long transactionsForUserId) {
//        return transactionsForUserId.get(transactionsForUserId);
//    }

    public Collection<TransactionModel> getAllTransactionModels() {
        Collection<TransactionModel> allTransactionModels = new ArrayList<TransactionModel>();
        for (HashSet<TransactionModel> value : transactionsForUserId.values()) {
            for (TransactionModel model : value) {
                allTransactionModels.add(model);
            }
        }
        return allTransactionModels;
    }

//    public Collection<TransactionModel> getAllTransactions(){
//        HashSet<TransactionModel> transactionList;
//
//
//        return transactionsForUserId.values(); }
//
//
    public double calculatePoints(double amount){
//        for(TransactionModel transactionModel : transactionsForUserId
        return amount * 0.2;
    }
}
