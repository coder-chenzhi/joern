package sample;

import cfg.CFG;
import tests.udg.CFGCreator;

public class TestControlFlowGraph {
    public static void main(String[] args) {
        String code = "int ddg_test_struct(){ struct my_struct foo; foo.bar = 10; copy_somehwere(foo); }";
        CFGCreator cfgCreator = new CFGCreator();
        CFG cfg = cfgCreator.getCFGForCode(code);
        System.out.println(cfg);
    }
}
