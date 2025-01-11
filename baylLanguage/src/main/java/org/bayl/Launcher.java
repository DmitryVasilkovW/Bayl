package org.bayl;

import org.bayl.vm.impl.VirtualMachineImpl;
import java.io.File;
import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        VirtualMachineImpl virtualMachine = new VirtualMachineImpl();
        virtualMachine.eval(new File(args[0]));
        //TODO Print nice error messages instead of throwing IOExceptions
    }
}
