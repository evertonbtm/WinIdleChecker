package br.winstate;

import br.winstate.service.message.MessageService;
import br.winstate.service.state.WinIdleService;
import br.winstate.util.IdleCheckerUtil;
import br.winstate.util.KafkaConfigUtil;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdleChecker {


    static boolean shutdown = false;
    static MessageService messageService = new MessageService();

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        while(!shutdown){
            Thread.sleep(IdleCheckerUtil.idleTime);
            executorService.submit(IdleChecker::run);

            var timestamp = Instant.now().getEpochSecond();

            if(timestamp % 5 == 0){
                try {
                    messageService.retrieve(KafkaConfigUtil.topic);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        executorService.shutdown();
    }

    public static void run(){
        System.out.println("Task executing in thread: " + Thread.currentThread().getName());
        WinIdleService idleChecker = new WinIdleService(IdleCheckerUtil.activeTime, IdleCheckerUtil.idleTime);
        idleChecker.start();
    }
}