# graphutils包
- Edge：边
```
// 两个私有成员变量 
private V destination;
private V source;

public V getDestination()
public V getSource()
```
- AbstractGraph：无向图
```
// 两个私有成员变量
private List<V> vertices;
private MultiHashMap<V, E> outNeighborhood;

public List<V> getVertices()
public List<E> getEdges()
public List<E> outgoingEdges(V src)
public boolean isConnected(V src, V dst)
public int outDegree(V vertex)
public boolean contains(V vertex)
public boolean isEmpty()
public int size()
public int numberOfEdges()
public boolean addVertex(V vertex)
public void removeVertex(V vertex)
public void addEdge(E edge)
public void removeEdge(E edge)
public void removeEdge(V src, V dst)
public void removeEdgesFrom(V source)
public void removeEdgesTo(V destination)
public void failIfNotContained(V vertex)
```
- AbstractTwoWayGraph：有向图，继承了AbstractGraph<V, E>
```
//增加了一个私有成员变量
private MultiHashMap<V, E> inNeighborhood;
//增加了2个方法
public List<E> ingoingEdges(V dst)
public int inDegree(V vertex)
//重写了4个方法
public void addEdge(E edge)
public void removeEdgesFrom(V source)
public void removeEdgesTo(V destination)
public void removeEdge(V src, V dst)
```

- PostorderIterator，图的后续遍历，实现了Iterator接口
```
public boolean hasNext()
public V next()
public void remove()
```


# misc包
- Pair：二元组
- MultiHashMap：一对多的HashMap，封装了HashMap<K, List<V>>
- HashMapOfSets：一对多的HashMap，不同的是底层封装了HashMap<Object, HashSet<Object>>