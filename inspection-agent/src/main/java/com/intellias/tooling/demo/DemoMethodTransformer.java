package com.intellias.tooling.demo;

import com.intellias.tooling.MethodIdentifier;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class DemoMethodTransformer implements AgentBuilder.Transformer {

    private final MethodIdentifier methodIdentifier;

    public DemoMethodTransformer(MethodIdentifier methodIdentifier) {
        this.methodIdentifier = methodIdentifier;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule module) {
        return builder.visit(
                Advice.to(ProcessMethodAdvice.class)
                        .on(ElementMatchers.named(methodIdentifier.getMethodName()))
        );
    }
}
