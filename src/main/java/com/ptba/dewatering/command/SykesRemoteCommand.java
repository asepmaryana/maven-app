package com.ptba.dewatering.command;

public class SykesRemoteCommand implements RemoteCommand {

    public SykesRemoteCommand() {}
    
    @Override
    public String execute(String device) {
        System.out.println("Execute SykesRemoteCommand ...");
        return "OK";
    }

}
