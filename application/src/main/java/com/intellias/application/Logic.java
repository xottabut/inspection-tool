package com.intellias.application;

import lombok.SneakyThrows;

public class Logic {

    @SneakyThrows
    public void mainProcessingLogic() {
        int i = 0;
        while (process(i)) {
            i++;
        }
    }

    private boolean process(int i) throws InterruptedException {
        System.out.println("Check... " + i);
        Thread.sleep(2000);
        return true;
    }

}
