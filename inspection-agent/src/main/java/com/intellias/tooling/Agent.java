package com.intellias.tooling;

import com.intellias.tooling.demo.DemoMethodTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("Premain attached");
        instrumentation.addTransformer(
                (loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
                    System.out.println("Transforming " + className);
                    // Create your own transformer using some bytecode manipulation lib
                    return null;
                }
        );
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        MethodIdentifier methodIdentifier = AgentArgumentsParser.parseMethodIdentifier(args);

        new AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(methodIdentifier.getClassName()))
                .transform(new DemoMethodTransformer(methodIdentifier))
                .installOn(instrumentation);
    }

}
