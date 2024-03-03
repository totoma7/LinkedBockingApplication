package com.example.linkedbocking;

import java.util.concurrent.CompletableFuture;

public class SequentialWorkManager {
    private CompletableFuture<Void> future;

    public SequentialWorkManager() {
        // CompletableFuture 초기화
        this.future = new CompletableFuture<>();
    }

    public void startProcessing(String job) throws InterruptedException {
        // 여기에 실제 작업 처리 로직을 구현합니다.
        // 예시로, 단순히 "작업 처리 중..." 메시지를 출력하고, 작업을 완료합니다.
        for (int i = 0; i < 30; i++) {
            System.out.println("seq :"+i);
            Thread.sleep(300);
        }
        // 작업 처리가 완료되면 CompletableFuture를 완료 상태로 설정합니다.
        this.future.complete(null); // null은 Void 타입의 결과를 나타냅니다.
    }

    public void waitForCompletion() throws InterruptedException {
        // CompletableFuture의 결과가 준비될 때까지 현재 스레드를 블로킹합니다.
        // get() 메서드는 완료될 때까지 현재 스레드를 대기시키며, 완료되면 결과를 반환합니다.
        // 여기서는 반환값이 없으므로 단순히 완료를 기다립니다.
        try {
            this.future.get();
        } catch (Exception e) {
            // 예외 처리: InterruptedException 또는 ExecutionException 처리
            // 여기서는 InterruptedException만 처리합니다.
            Thread.currentThread().interrupt();
            throw new InterruptedException("작업 대기 중 인터럽트 발생");
        }
    }
}