package com.w_angler.ethereum.explorer.controller;

import com.w_angler.ethereum.explorer.response.Response;
import com.w_angler.ethereum.explorer.response.Status;
import com.w_angler.ethereum.explorer.service.EthService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.Map;

import static java.util.Objects.isNull;

@Controller
public class EthController {
    private final EthService ethService;

    @Autowired
    public EthController(EthService ethService) {
        this.ethService = ethService;
    }

    /**
     * get newest block (for stomp)
     *
     * @param block newest block
     * @return newest block
     */
    @SendTo("/topic/blocks")
    public EthBlock.Block blocks(EthBlock.Block block) {
        return block;
    }

    /**
     * get newest transaction (for stomp)
     *
     * @param transaction newest transaction
     * @return newest transaction
     */
    @SendTo("/topic/transactions")
    public Transaction transactions(Transaction transaction) {
        return transaction;
    }

    /**
     * get latest 10 blocks
     *
     * @return latest 10 blocks
     */
    @RequestMapping(value = "/eth/block/latest", method = RequestMethod.GET)
    @ResponseBody
    public Response latestBlocks() {
        val blocks = ethService.latestBlocks();
        return isNull(blocks)
                ? new Response(Status.ETH_GET_LATEST_BLOCK_FAILED)
                : new Response(Status.SUCCESS, blocks);
    }

    /**
     * get latest 10 transactions
     *
     * @return latest 10 transactions
     */
    @RequestMapping(value = "/eth/transaction/latest", method = RequestMethod.GET)
    @ResponseBody
    public Response latestTransactions() {
        val transactions = ethService.latestTransactions();
        return isNull(transactions)
                ? new Response(Status.ETH_GET_LATEST_TX_FAILED)
                : new Response(Status.SUCCESS, transactions);
    }

    /**
     * get all blocks
     *
     * @param model template model
     * @param page  page number
     * @return template
     */
    @RequestMapping("/eth/blocks")
    public String allBlocks(Map<String, Object> model,
                            @RequestParam(required = false, defaultValue = "1") int page) {
        val blocks = ethService.allBlocks(model, page);
        model.put("page", page);
        model.put("hasErrors", isNull(blocks));
        model.put("blocks", blocks);
        return "eth/blocks";
    }

    /**
     * get all transactions
     *
     * @return template
     */
    @RequestMapping("/eth/transactions")
    public String allTransactions(Map<String, Object> model,
                                  @RequestParam String blockHash) {
        val transactions = ethService.allTransactions(model, blockHash);
        model.put("transactions", transactions);
        model.put("hasErrors", isNull(transactions));
        return "eth/transactions";
    }

    /**
     * get specified block
     *
     * @param hash block hash
     * @return template
     */
    @RequestMapping("/eth/block/{hash}")
    public String block(Map<String, Object> model,
                        @PathVariable String hash) {
        val block = ethService.block(hash);
        model.put("hasErrors", isNull(block));
        model.put("block", block);
        return "eth/block";
    }

    /**
     * get specified transaction
     *
     * @param hash transaction
     * @return template
     */
    @RequestMapping("/eth/transaction/{hash}")
    public String transaction(Map<String, Object> model,
                              @PathVariable String hash) {
        val transaction = ethService.transaction(hash);
        model.put("hasErrors", isNull(transaction));
        model.put("transaction", transaction);
        return "eth/transaction";
    }

    /**
     * block search
     *
     * @param blockNumberOrHash block number Or block hash
     * @return block
     */
    @RequestMapping(path = "/eth/block/search", method = RequestMethod.POST)
    @ResponseBody
    public Response searchBlock(@RequestParam("keyword") String blockNumberOrHash) {
        val block = ethService.searchBlock(blockNumberOrHash);
        return isNull(block)
                ? new Response(Status.ETH_GET_BLOCK_FAILED)
                : new Response(Status.SUCCESS, block);
    }

    /**
     * transaction search
     *
     * @param hash transaction hash
     * @return template
     */
    @RequestMapping(path = "/eth/transaction/search", method = RequestMethod.POST)
    @ResponseBody
    public Response searchTx(@RequestParam("keyword") String hash) {
        val tx = ethService.searchTx(hash);
        return isNull(tx)
                ? new Response(Status.ETH_GET_TX_FAILED)
                : new Response(Status.SUCCESS, tx);
    }

    /**
     * explore account information (not implemented yet)
     *
     * @param model   template model
     * @param address account address
     * @return template
     */
    @RequestMapping("/eth/account/{address}")
    public String account(Map<String, Object> model,
                          @PathVariable String address) {
        val account = ethService.account(address);
        model.put("account", account);
        model.put("address", address);
        model.put("hasErrors", isNull(account));
        return "eth/account";
    }
}
