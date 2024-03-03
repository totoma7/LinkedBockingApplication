package com.example.linkedbocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


@SpringBootApplication
public class LinkedBockingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkedBockingApplication.class, args);
    }

}
@RestController
class SimpleController {

    private static final LinkedBlockingQueue<SequentialWorkManager> workQueue = new LinkedBlockingQueue<>();

    static {
        // 작업 처리 스레드 초기화 및 시작
        Thread workerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 큐에서 작업을 가져와서 처리. 큐가 비어있으면 작업이 들어올 때까지 대기
                    SequentialWorkManager manager = workQueue.take();
                    // 실제 작업 처리
                    manager.startProcessing("job");
                    // 처리가 끝날 때까지 기다림 (waitForCompletion 메서드 구현 필요)
                    manager.waitForCompletion();
                } catch (InterruptedException e) {
                    // 스레드 인터럽트 상태를 복원하고 루프를 종료하여 스레드를 정상적으로 종료
                    Thread.currentThread().interrupt();
                }
            }
        });
        workerThread.start();
    }

    @GetMapping("/call")
    public String sayHello() {
        SequentialWorkManager manager = new SequentialWorkManager();
        // 작업 큐에 작업 추가. LinkedBlockingQueue는 스레드 안전하므로 동기화 블록 필요 없음
        workQueue.add(manager);
        return "작업이 큐에 추가되었습니다.";
    }
}