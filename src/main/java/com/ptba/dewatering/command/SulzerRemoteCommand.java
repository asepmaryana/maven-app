package com.ptba.dewatering.command;

public class SulzerRemoteCommand implements RemoteCommand {

    public SulzerRemoteCommand() {}
    
    @Override
    public String execute(String device) {
        System.out.println("Execute SulzerRemoteCommand ...");
        return "OK";
    }

}
