package br.winstate;

import br.winstate.service.message.MessageService;
import br.winstate.service.state.IdleCheckerService;
import br.winstate.service.state.WinIdleService;
import br.winstate.util.IdleCheckerUtil;
import br.winstate.util.KafkaConfigUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdleChecker {
    private static final Logger logger = LogManager.getLogger(IdleChecker.class);

    public static void main(String[] args) throws InterruptedException {
        IdleCheckerService idleCheckerService = new IdleCheckerService();
        idleCheckerService.startService();
    }

}