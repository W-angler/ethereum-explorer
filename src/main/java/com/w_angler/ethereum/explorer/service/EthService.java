package com.w_angler.ethereum.explorer.service;

import com.w_angler.ethereum.explorer.utils.CheckUtil;
import com.w_angler.ethereum.explorer.utils.TimeUtil;

import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.parity.Parity;

import java.io.IOException;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class EthService {
    private final Parity parity;

    @Autowired
    public EthService(Parity parity) {
        this.parity = parity;
    }

    public List<EthBlock.Block> latestBlocks() {
        val blocks = new ArrayList<EthBlock.Block>();
        try {
            val latest = parity.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), true).send().getBlock();
            blocks.add(latest);
            String parentHash = latest.getParentHash();
            for (int i = 0; i < 9; i++) {
                val parentBlock = parity.ethGetBlockByHash(parentHash, true).send().getBlock();
                parentHash = parentBlock.getParentHash();
                blocks.add(parentBlock);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return blocks;
    }

    public List<Transaction> latestTransactions() {
        val transactions = new ArrayList<Transaction>();
        try {
            val latest = parity.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), true).send().getBlock();
            val earliest = parity.ethGetBlockByNumber(DefaultBlockParameter.valueOf("earliest"), true).send().getBlock();
            String hash = latest.getHash();
            String parentHash = latest.getParentHash();
            while (transactions.size() < 10 && !Objects.equals(hash, earliest.getHash())) {
                val count = parity.ethGetBlockTransactionCountByHash(hash).send().getTransactionCount();
                for (BigInteger i = BigInteger.ZERO; i.compareTo(count) == -1; i = i.add(BigInteger.ONE)) {
                    val transaction = parity.ethGetTransactionByBlockHashAndIndex(hash, i).send().getTransaction();
                    transaction.ifPresent(transactions::add);
                }
                hash = parentHash;
                val parentBlock = parity.ethGetBlockByHash(parentHash, true).send().getBlock();
                parentHash = parentBlock.getParentHash();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return transactions;
    }

    public List<EthBlock.Block> allBlocks(Map<String, Object> model, int page) {
        if (page <= 0) {
            return null;
        }
        val blocks = new ArrayList<EthBlock.Block>();
        try {
            val latest = parity.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), true).send().getBlock();
            long number = latest.getNumber().longValue();
            long pages = number % 20 == 0 ? number / 20 : number / 20 + 1;
            model.put("pages", pages);
            if (page > pages) {
                return null;
            }
            val first = parity.ethGetBlockByNumber(
                    DefaultBlockParameter.valueOf(latest.getNumber()
                            .subtract(BigInteger.valueOf((page - 1) * 20))), true)
                    .send().getBlock();
            blocks.add(first);
            String parentHash = first.getParentHash();
            long blockNumber = first.getNumber().longValue();
            while (blocks.size() < 20 && blockNumber > 0) {
                val block = parity.ethGetBlockByHash(parentHash, true).send().getBlock();
                blocks.add(block);
                parentHash = block.getParentHash();
                blockNumber--;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return blocks;
    }

    public List<Transaction> allTransactions(Map<String, Object> model, String blockHash) {
        val transactions = new ArrayList<Transaction>();
        try {
            val block = parity.ethGetBlockByHash(blockHash, true).send().getBlock();
            if (isNull(block)) {
                return null;
            }
            model.put("blockNumber", block.getNumber());
            val count = parity.ethGetBlockTransactionCountByHash(blockHash).send().getTransactionCount();
            if (isNull(count)) {
                return null;
            }
            for (BigInteger i = BigInteger.ZERO; i.compareTo(count) == -1; i = i.add(BigInteger.ONE)) {
                val transaction = parity.ethGetTransactionByBlockHashAndIndex(blockHash, i).send().getTransaction();
                transaction.ifPresent(transactions::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return transactions;
    }

    public EthBlock.Block block(String hash) {
        try {
            val block = parity.ethGetBlockByHash(hash, true).send().getBlock();
            if (nonNull(block)) {
                block.setTimestamp(TimeUtil.dateToLocalDateTime(new Date(block.getTimestamp().longValue() * 1000))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            return block;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Transaction transaction(String hash) {
        try {
            return parity.ethGetTransactionByHash(hash).send().getTransaction().orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EthBlock.Block searchBlock(String blockNumberOrHash) {
        //Block number
        if (blockNumberOrHash.matches("\\d+")) {
            try {
                return parity.ethGetBlockByNumber(DefaultBlockParameter.valueOf(
                        BigInteger.valueOf(Long.parseLong(blockNumberOrHash))), true)
                        .send().getBlock();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        //Block hash
        else if (CheckUtil.isHex(blockNumberOrHash)) {
            try {
                return parity.ethGetBlockByHash(blockNumberOrHash, true)
                        .send().getBlock();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Transaction searchTx(String hash) {
        if (CheckUtil.isHex(hash)) {
            try {
                return parity.ethGetTransactionByHash(hash)
                        .send().getTransaction().orElse(null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String account(String address) {
        //TODO get account information
        return "account";
    }
}
