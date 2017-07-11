package com.w_angler.ethereum.explorer.config;

import com.w_angler.ethereum.explorer.prop.EthProperties;
import lombok.extern.log4j.Log4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

@Log4j
@Configuration
public class Web3jConfiguration {
    @Autowired
    private EthProperties ethProperties;
    @Autowired
    private SimpMessagingTemplate template;

    @Bean
    public Parity getParity() {
        val peers = ethProperties.getPeers();
        Parity parity;
        if (peers.isEmpty()) {
            parity = Parity.build(new HttpService());
            log.info("connected to http://localhost:8545");
        } else {
            parity = connect(peers);
        }
        parity.blockObservable(false).subscribe(ethBlock -> template.convertAndSend("/topic/blocks", ethBlock.getBlock()));
        parity.transactionObservable().subscribe(transaction -> template.convertAndSend("/topic/transactions", transaction));
        return parity;
    }

    private Parity connect(List<String> peers) {
        Parity parity = null;
        for (String peer : peers) {
            log.info("connecting to " + peer);
            parity = Parity.build(new HttpService(peer));
            log.info("connected to " + peer);
            try {
                parity.netVersion().send().getNetVersion();
                break;
            } catch (IOException e) {
                parity = null;
                log.error("connecting to " + peer + " failed: " + e.getMessage());
            }
        }
        if (isNull(parity)) {
            parity = Parity.build(new HttpService());
            log.info("connected to http://localhost:8545");
        }
        return parity;
    }
}
