控制流图CFG(Control Flow Graph)

## node包
node包里面定义了CFG所需的节点
CFGNode是一个接口，只有一个getProperties()方法
AbstractCFGNode实现了CFGNode接口，但只是初始化了properties
```java
properties.put(NodeKeys.CODE, toString());
properties.put(NodeKeys.TYPE, getClass().getSimpleName());
properties.put(NodeKeys.IS_CFG_NODE, "True");
```
以下类型继承了AbstractCFGNode
- CFGEntryNode
- CFGExitNode
- CFGErrorNode
- EmptyBlock
- InfiniteForNode
- CFGErrorNode
- ASTNodeContainer

## edge和graph
- CFGEdge：继承了Edge
```java
//增加了两个私有成员变量
private String label;
private Map<String, Object> properties;
```
- CFG：继承了AbstractTwoWayGraph<CFGNode, CFGEdge>，所有CFG至少包含entry节点和exit节点，它们是两个虚节点，没有任何实际信息
```java
// 增加了4个私有成员变量
private CFGEntryNode entry;
private CFGExitNode exit; 
private CFGErrorNode error;
private List<CFGNode> parameters; // 每个参数都会被作为一个CFG Node
// 重写1个方法
public boolean isEmpty()
//新增方法
public CFGNode getExitNode()
public CFGNode getEntryNode()
public CFGNode getErrorNode()
public void registerParameter(CFGNode parameter)
public List<CFGNode> getParameters()
public void addCFG(CFG otherCFG)
public void appendCFG(CFG otherCFG)
public void mountCFG(CFGNode branchNode, CFGNode mergeNode, CFG cfg, String label)
private void addVertices(CFG cfg)
private void addEdges(CFG cfg)
public void addEdge(CFGNode srcBlock, CFGNode dstBlock)
public void addEdge(CFGNode srcBlock, CFGNode dstBlock, String label)
```

- CFGFactory：生成CFG的工厂类，每种语言需要实现各自的CFGFactory
```java
public CFG newInstance(FunctionDef functionDefinition)
```
- ASTToCFGConverter
```java
public CFG convert(FunctionDef node) // 将AST中的方法定义节点转化为CFG
```


## cfg.C包

- CCFG：CFG的C语言定制版
```java
// 增加了5个新的私有成员变量，breakStatements\continueStatements\returnStatements\gotoStatements都是产生分支的语句，不支持Switch语句吗？
private List<CFGNode> breakStatements;
private List<CFGNode> continueStatements;
private List<CFGNode> returnStatements;
private HashMap<CFGNode, String> gotoStatements;
private HashMap<String, CFGNode> labels;
//
public List<CFGNode> getBreakStatements()
public void setBreakStatements(List<CFGNode> breakStatements)
public void addBreakStatement(CFGNode statement)
```

- CCFGFactory，构建CFG，按照AST的结构遍历AST节点（由structuredFlowVisitior控制），挨个转化为CFG，顺序语句就把相邻的CFG相连，其他语句看情况把该连的CFG连起来，比如while语句需要将condition节点与while的body节点（True Condition）以及while的exit节点相连（False Condition）。除此之外，break语句、continue语句、goto语句和return语句，需要在特定的节点遍历完成之后，再统一修复它的CFG
```java
// 1个私有变量
private static StructuredFlowVisitor structuredFlowVisitior = new StructuredFlowVisitor();
// 20个方法
public CFG newInstance(FunctionDef functionDefinition)
public static CCFG newInstance(ASTNode... nodes)
public static CCFG newErrorInstance()
public static CFG newInstance(IfStatement ifStatement)
public static CFG newInstance(WhileStatement whileStatement)
public static CFG newInstance(ForStatement forStatement)
public static CFG newInstance(DoStatement doStatement)
public static CFG newInstance(SwitchStatement switchStatement)
public static CFG newInstance(ParameterList paramList)
public static CFG newInstance(CompoundStatement content)
public static CFG newInstance(ReturnStatement returnStatement)
public static CFG newInstance(GotoStatement gotoStatement)
public static CFG newInstance(Label labelStatement)
public static CFG newInstance(ContinueStatement continueStatement)
public static CFG newInstance(BreakStatement breakStatement)
public static CCFG convert(ASTNode node)
public static void fixBreakStatements(CCFG thisCFG, CFGNode target)
public static void fixContinueStatement(CCFG thisCFG, CFGNode target)
public static void fixGotoStatements(CCFG thisCFG)
public static void fixReturnStatements(CCFG thisCFG)
```

