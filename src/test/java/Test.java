import java.io.File;
import java.io.IOException;

import org.bayl.vm.impl.VirtualMachineImpl;

public class Test {
    public static void main(String[] args) throws IOException {
        VirtualMachineImpl interpreter = new VirtualMachineImpl();
        interpreter.eval(new File("sample.bl"));
    }
}
