package org.bayl.ast;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidOperatorException;
import org.bayl.runtime.exception.TypeMismatchException;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;

public abstract class RelationalOpNode extends BinaryOpNode {

    public RelationalOpNode(SourcePosition pos, String operator, Node left, Node right) {
        super(pos, operator, left, right);
    }
}
