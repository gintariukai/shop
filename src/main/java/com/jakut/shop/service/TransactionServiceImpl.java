package com.jakut.shop.service;

import com.jakut.shop.model.Transaction;
import com.jakut.shop.repository.TransactionRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Data
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Long numberOfTransactions() {
        return transactionRepository.count();
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }
}
