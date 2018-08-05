编译成功后会得到3个Jar包，joern.jar、icfg.jar和argumentTainter.jar，
三者的入口分别是tools.index.IndexMain、tools.icfg.ICFGMain和tools.argumentTainter.ArgumentTainterMain
项目所依赖的ANTRL不是官方版本，是非官方的优化版本（https://github.com/tunnelvisionlabs/antlr4）

joern.jar是最核心的，
argumentTainter.jar

1. 源码 - 词法分析 - Token  
2. Token - 语法分析 - ParseTree (Concrete Syntax Tree) 
3. ParseTree (Concrete Syntax Tree) - ASTBuilder - AST (Abstract Syntax Tree)  
抽象语法树是具体语法树的紧密表示形式，更易于进行程序分析，
比如大括号、分号等冗余的结构都删除了，方法的访问控制符、参数列表等都作为方法节点的属性存在，而不是单独的节点
4. AST - CFG


第一步和第二步都是ANTLR4完成的,antlr.C包中的代码就是借助ANTLR生成的，
主要包括Function和Module两部分，前者对应方法，后者对应一个源码文件，具体的类包括：
- ModuleLexer：Module的词法分析器，输入是ANTLRInputStream，输出是TokenStream
- ModuleParser：Module的语法分析器，输入是TokenStream，输出是ParseTree
- ModuleListener：Module的监听器，在使用ParseTreeWalker遍历ParseTree时，
可以针对不同类型的节点调用不同的方法实现不同的功能，AST就是通过Listener实现的
（其实Visitor也可以实现这个功能，但是使用上稍有不同）
- ModuleBaseListener：Module的基础监听器
- FunctionLexer：Function的词法分析器
- FunctionParser：Function的语法分析器
- FunctionBaseListener：Function的基础监听器


## ast包和parsing包
第三步是Joern自己完成的，ast包中定义了抽象语法树中的所有节点类型，
parsing包中包含了所有与语法解析相关的类

ASTWalker负责管理如何遍历AST，ASTWalker是一个Observer，

ASTNodeVisitor负责管理遍历AST时，针对不同节点进行不同操作

ANTLRParserDriver是核心类，是一个Observable，主要有两个功能，
一是Parser的driver类，从String中生成ParseTree
```
public void parseAndWalkFile(String filename) throws ParserException { ... }
public void parseAndWalkTokenStream(TokenSubStream tokens) throws ParserException { ... }
public ParseTree parseAndWalkString(String input) throws ParserException { ... }
public ParseTree parseTokenStream(TokenSubStream tokens) throws ParserException { ... }
public ParseTree parseString(String input) throws ParserException { ... }
protected TokenSubStream createTokenStreamFromFile(String filename) throws ParserException { ... }
```
一是AST的provider类，当AST构建完成时会通知相关的watcher，这个是通过观察者模式实现的，下面的方法实现这些功能
```
public void begin() { notifyObserversOfBegin(); }
public void end() { notifyObserversOfEnd(); }
private void notifyObserversOfBegin()
{
    ASTWalkerEvent event = new ASTWalkerEvent(ASTWalkerEvent.eventID.BEGIN);
    setChanged();
    notifyObservers(event);
}
private void notifyObserversOfEnd() { ... }
public void notifyObserversOfUnitStart(ParserRuleContext ctx) { ... }
public void notifyObserversOfUnitEnd(ParserRuleContext ctx) { ... }
public void notifyObserversOfItem(ASTNode aItem) { ... }
```
FunctionParser是ANTLRParserDriver的子类，负责解析Function

ModuleParser是ANTLRParserDriver的子类，负责解析Module

TokenSubStream是BufferedTokenStream的子类，不知道为什么要写一个子类

CModuleParserTreeListener，将ParserTree转化为AST


## fileWalker包
fileWalker包是一些文件遍历相关的类
FileNameMatcher是用于过滤文件的，默认是匹配.c/.h/.cpp/.hpp/.java文件，通常是作为Walker的一个成员变量，
由Walker调用来判断是否需要访问这个文件
SourceFileListener是监听访问目录的一些行为，如进入目录、离开目录、访问文件等，将Listener注册到Walker，
然后walker在发生以上行为时，调用Listener的对应方法


## outputModule包
outputModule是输出数据相关的类  
neo4j包是输出到neo4j数据库，
Neo4JIndexer是主类，它会调用Neo4JASTWalker遍历AST，调用Neo4JDirectoryTreeImporter遍历目录  
Neo4JASTWalker会在访问AST的各个节点时会调用Neo4JASTNodeVisitor对应的方法，包括
```
public void visit(FunctionDef node) # 调用FunctionImporter()输出到Neo4J
public void visit(ClassDefStatement node) # 调用ClassDefImporter()输出到Neo4J
public void visit(IdentifierDeclStatement node) # 调用DeclStmtImporter()输出到Neo4J
```
需要注意的是，FunctionImporter中定义了如下的成员变量
```
ASTImporter astImporter = new ASTImporter(nodeStore);
CFGImporter cfgImporter = new CFGImporter(nodeStore);
UDGImporter udgImporter = new UDGImporter(nodeStore);
DDGImporter ddgImporter = new DDGImporter(nodeStore);
CDGImporter cdgImporter = new CDGImporter(nodeStore);
```