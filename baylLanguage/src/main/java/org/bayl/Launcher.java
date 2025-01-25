package org.bayl;

import org.bayl.vm.impl.VirtualMachineImpl;

import java.io.File;

public class Launcher {
    public static void main(String[] args) {
        VirtualMachineImpl virtualMachine = new VirtualMachineImpl();
        try {
            virtualMachine.eval(new File(args[0]));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
