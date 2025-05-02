package com.ptba.dewatering;

import com.ptba.dewatering.command.RemoteCommand;

public class Main {
    public static void main( String[] args ) throws Exception
    {
        if (args.length == 0) System.out.println("Required 1 argument : [name]");
        else {
            String className = args[0].toLowerCase().equals("sulzer") ? "com.ptba.dewatering.command.SulzerRemoteCommand" : "com.ptba.dewatering.command.SykesRemoteCommand";
            Class<?> cls = ClassLoader.getSystemClassLoader().loadClass(className);
            RemoteCommand rCommand = (RemoteCommand) cls.newInstance();
            rCommand.execute("PP209");
        }
    }
}
