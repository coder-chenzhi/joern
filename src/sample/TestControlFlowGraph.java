package sample;

import cfg.CFG;
import tests.udg.CFGCreator;

public class TestControlFlowGraph {
    public static void main(String[] args) {
        String testFilePathPrefix = "E:\\Code\\IDEA\\joern\\testCode";
        String testFile = testFilePathPrefix + "Vul\\Firefox_48.0b9_CVE_2016_5257_frame_buffer.cc";
//        String code = "int ddg_test_struct(){ struct my_struct foo; foo.bar = 10; copy_somehwere(foo); }";
        String code = "VCMSessionInfo::PacketIterator VCMSessionInfo::FindNextPartitionBeginning(PacketIterator it, int test) const {   while (it != packets_.end()) {     if ((*it).codecSpecificHeader.codecHeader.VP8.beginningOfPartition) {       return it;     }     ++it;   }   return it; }";
        CFGCreator cfgCreator = new CFGCreator();
        CFG cfg = cfgCreator.getCFGForCode(code);
        System.out.println(cfg);
    }
}
