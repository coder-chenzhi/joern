## 控制依赖图CDG（Control Dependency Graph）
> A node (basic block) Y is control-dependent on another X iff X determines whether Y executes, i.e.
> - there exists a path from X to Y such that every node in the path other than X & Y is post-dominated by Y
> - X is not post-dominated by Y (也就是说，至少存在一条经过X的执行路径可以不通过Y)

> A node _d_ dominates a node _n_ if every path from the entry node to _n_ must go through _d_.  
> A node _z_ is said to post-dominate a node _n_ if all paths to the exit node of the graph starting at _n_ must go through _z_.  
> A node d strictly dominates a node n if d dominates n and d does not equal n.  
> The immediate dominator or idom of a node n is the unique node that strictly dominates n but does not strictly dominate any other node that strictly dominates n. Every node, except the entry node, has an immediate dominator.  
> The dominance frontier of a node d is the set of all nodes n such that d dominates an immediate predecessor of n, but d does not strictly dominate n. It is the set of nodes where d's dominance stops.  
> A dominator tree is a tree where each node's children are those nodes it immediately dominates. Because the immediate dominator is unique, it is a tree. The start node is the root of the tree.

构建一棵逆向控制流图的支配树，利用支配树计算出所有节点的支配边界。
逆向控制流图上结点的支配边界对应控制流图上该结点所依赖的分支结点。
利用这一特点，通过为逆向控制流图上每一个结点到其支配边界上的点增加控制依赖边，就建立了原控制流图上的控制依赖。

实现方式是按照下面这篇文章的方法
> Cooper, Keith D., Timothy J. Harvey, and Ken Kennedy. 
"A simple, fast dominance algorithm." Software Practice & Experience 4.1-10 (2001): 1-8.

- DominatorTree
```java
private HashMap<V, V> dominators; // 实际上是immediate dominator
private HashMap<V, Set<V>> dominanceFrontiers; // 每个节点的dominator set
private HashMap<V, Integer> postorderEnumeration; // 后续遍历的节点列表，V是节点，Integer是节点在遍历时的顺序
```

- DominatorTreeCreator
```java
// 成员变量
private DominatorTree<V> dominatorTree;
private AbstractTwoWayGraph<V, E> graph;
private List<V> orderedVertices; // 后续遍历的节点列表
private V startNode;
// 构造方法，指定有向图及开始节点
public DominatorTreeCreator(AbstractTwoWayGraph<V, E> graph, V startNode)


```