# Cpp常用STL方法摘录

## vector
###  定义与初始化
* vector<T>  v1;
* vector<T> v1(v2);
* vector<T> v1=v2;
* vector<T> v1;
* vector<T> v1(n,val);
* vector<T> v1{a,b,c,d...}; 
* vector<T> v1={a,b,c,d...};

###  常用函数
* void  v.push_back(x)
* iterator v.insert(it, x):在迭代器it所指元素前插入元素x
* iterator v.erase(it):删除迭代器it所指处的元素
* iterator v.erase(first_it, last_it):删除it所指区间内的元素
* void v.pop_back():删除vector最后一个元素
* void v.clear():删除所有元素
* v.front():返回v[0]
* v.back():返回v[size-1]
* v.at()
* v.begin()
* v.end()
* v.max_size()
* v.capacity()
* v.size()
* v.resize()
* v.erase()
* 
* v.rbegin()
* v.rend()
* v.empty()
* v.swap()

## queue
* que.back()
* que.front()
* que.push()
* que.pop()
* que.empty()
* que.size()

## stack
* st.top()
* st.push()
* st.pop()
* st.empty()
* st.size()

## set
* set.begin()
* set.end()
* set.count()
* set.size()


## map
* hash.begin()
* hash.end()
* hash.count()
* hash.find()
* hash.lower_bound()
* hash.upper_bound()
* hash.insert()
* hash.rbegin()
* hash.rend()
* hash.swap()
* hash.value_comp()
* hash.
