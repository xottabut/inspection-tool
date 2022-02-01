package com.intellias.tooling;

public final class AgentArgumentsParser {

    public static MethodIdentifier parseMethodIdentifier(String args) {
        String[] tokens = args.split(":");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Three tokens expected divided by colon: x:y:z " + "where x - class name, y - method name, z - file with method implementation");
        }
        return new MethodIdentifier(tokens[0], tokens[1]);
    }

}
