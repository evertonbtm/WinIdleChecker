package br.winstate.service.state;

import br.winstate.service.message.MessageService;
import br.winstate.util.IdleCheckerUtil;
import br.winstate.util.KafkaConfigUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdleCheckerService {

     boolean shutdown = false;

    public void startService() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        while(!shutdown){
            Thread.sleep(IdleCheckerUtil.threadCheckTime);
            executorService.submit(this::runPublisher);
            executorService.submit(this::runReceiver);
        }

        executorService.shutdown();
    }

    public void runPublisher(){
        System.out.println("Publish Task executing in thread: " + Thread.currentThread().getName());
        WinIdleService idleChecker = new WinIdleService(IdleCheckerUtil.activeTime, IdleCheckerUtil.idleTime);
        idleChecker.start();
    }

    public void runReceiver(){
        System.out.println("Receiver Task executing in thread: " + Thread.currentThread().getName());
        MessageService messageService = new MessageService();
        try {
            messageService.retrieve(KafkaConfigUtil.topic);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
