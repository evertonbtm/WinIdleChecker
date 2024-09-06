package br.winstate.util;

import java.util.Map;

public class MachineUtil {

    public static String getComputerName(){
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else return env.getOrDefault("HOSTNAME", "Unknown Computer");
    }

}
