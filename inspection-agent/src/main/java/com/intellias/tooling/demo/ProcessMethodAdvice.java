package com.intellias.tooling.demo;

import net.bytebuddy.asm.Advice;

public class ProcessMethodAdvice {

    @Advice.OnMethodEnter
    public static void onStart() {
        System.out.println("Time to finish! But you still have one try!");
    }

    @Advice.OnMethodExit
    public static void onFinish(@Advice.Return(readOnly = false) Boolean value) {
        System.out.println("Ok, that's enough. Stop!");
        value = false;
    }

}
