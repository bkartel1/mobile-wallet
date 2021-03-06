package org.mifos.mobilewallet.core.data.fineract.entity.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.Transactions;
import org.mifos.mobilewallet.core.domain.model.Transaction;
import org.mifos.mobilewallet.core.domain.model.TransactionType;
import org.mifos.mobilewallet.core.utils.DateHelper;

/**
 * Created by naman on 15/8/17.
 */

public class TransactionMapper {

    @Inject
    CurrencyMapper currencyMapper;

    @Inject
    public TransactionMapper() {}

    public List<Transaction> transformTransactionList(SavingsWithAssociations
                                                              savingsWithAssociations) {
        List<Transaction> transactionList = new ArrayList<>();

        if (savingsWithAssociations != null && savingsWithAssociations.getTransactions() != null
                && savingsWithAssociations.getTransactions().size() != 0) {

            for (Transactions transactions : savingsWithAssociations.getTransactions()) {
                transactionList.add(transformInvoice(transactions));
            }


        }
        return transactionList;
    }

    public Transaction transformInvoice(Transactions transactions) {
        Transaction transaction = new Transaction();

        if (transactions != null ) {

            transaction.setAmount(transactions.getAmount());

            if (transactions.getPaymentDetailData() != null) {
                transaction.setTransactionId(transactions
                        .getPaymentDetailData().getReceiptNumber());
            }

            if (transactions.getSubmittedOnDate() != null) {
                transaction.setDate(DateHelper.getDateAsString(transactions.getSubmittedOnDate()));
            }

            transaction.setCurrency(currencyMapper.transform(transactions.getCurrency()));
            transaction.setTransactionType(TransactionType.OTHER);

            if (transactions.getTransactionType().getDeposit()) {
                transaction.setTransactionType(TransactionType.CREDIT);
            }

            if (transactions.getTransactionType().getWithdrawal()) {
                transaction.setTransactionType(TransactionType.DEBIT);
            }

        }
        return transaction;
    }
}
