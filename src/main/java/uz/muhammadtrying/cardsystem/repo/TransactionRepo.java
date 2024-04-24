package uz.muhammadtrying.cardsystem.repo;

import uz.muhammadtrying.cardsystem.entity.Expenditure;
import uz.muhammadtrying.cardsystem.entity.Income;
import uz.muhammadtrying.cardsystem.entity.Transaction;
import uz.muhammadtrying.cardsystem.entity.enums.Type;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class TransactionRepo {
    public static List<Transaction> fetchTransactions() {
        IncomeRepo incomeRepo = new IncomeRepo();
        ExpenditureRepo expenditureRepo = new ExpenditureRepo();

        //fetch all
        List<Income> incomeRepoAll = incomeRepo.findAll();
        List<Expenditure> expenditureRepoAll = expenditureRepo.findAll();

        //sort them by time
        incomeRepoAll.sort(Comparator.comparing(Income::getCreatedAt));
        expenditureRepoAll.sort(Comparator.comparing(Expenditure::getCreatedAt));

        List<Transaction> transactionsOfIncome = incomeRepoAll
                .stream()
                .map(income -> new Transaction(
                        Type.INCOME, income.getAmount(),
                        income.getCreatedAt(),
                        income.getCard().getId()
                )).toList();
        List<Transaction> transactionsOfExpenditure = expenditureRepoAll.stream().map(expenditure -> new Transaction(Type.EXPENDITURE, expenditure.getAmount(), expenditure.getCreatedAt(), expenditure.getCard().getId())).toList();

        return Stream.concat(transactionsOfIncome.stream(), transactionsOfExpenditure.stream()).toList();
    }
}
