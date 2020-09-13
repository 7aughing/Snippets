# day90 flask

django是个大而全的框架，flask是一个轻量级的框架。

django内部为我们提供了非常多的组件：orm / session / cookie / admin / form / modelform / 路由 / 视图 / 模板 /  中间件 / 分页 / auth / contenttype  / 缓存 / 信号 / 多数据库连接 

flask框架本身没有太多的功能：路由/视图/模板(jinja2)/session/中间件 ，第三方组件非常齐全。 
注意：django的请求处理是逐一封装和传递； flask的请求是利用上下文管理来实现的。 

## 内容回顾

1. 什么是jwt？
2. cmdb的实现原理？
3. 都用到了那些命令？
4. 遇到过哪些bug？
5. 什么是开封封闭原则？

## 今日概要

- flask的快速使用
- 实现一个xx管理系统
- 蓝图



## 今日详细

### 1.flask快速使用

安装

```
pip3 install flask
```

#### 1.1 依赖wsgi Werkzeug

```python
from werkzeug.serving import run_simple

def func(environ, start_response):
    print('请求来了')
    pass

if __name__ == '__main__':
    run_simple('127.0.0.1', 5000, func)
```

```python
from werkzeug.serving import run_simple

class Flask(object):
    
    def __call__(self,environ, start_response):
        return "xx"
app = Flask()

if __name__ == '__main__':
    run_simple('127.0.0.1', 5000, app)
    
```

```python
from werkzeug.serving import run_simple

class Flask(object):
    
    def __call__(self,environ, start_response):
        return "xx"
    
    def run(self):
        run_simple('127.0.0.1', 5000, self)
        
app = Flask()

if __name__ == '__main__':
    app.run()
```

### 1.2 快速使用flask

```python
from flask import Flask

# 创建flask对象
app = Flask(__name__)

@app.route('/index')
def index():
    return 'hello world'


@app.route('/login')
def login():
    return 'login'

if __name__ == '__main__':
    app.run()
```

总结：

- flask框架是基于werkzeug的wsgi实现，flask自己没有wsgi。 
- 用户请求一旦到来，就会之 `app.__call__ `方法 。 
- 写flaks标准流程



### 1.3 用户登录&用户管理

```python
from flask import Flask, render_template, jsonify,request,redirect,url_for

app = Flask(__name__)

DATA_DICT = {
    1: {'name':'陈硕',"age":73},
    2: {'name':'汪洋',"age":84},
}

@app.route('/login',methods=['GET','POST'])
def login():
    if request.method == 'GET':
        # return '登录' # HttpResponse
        # return render_template('login.html') # render
        # return jsonify({'code':1000,'data':[1,2,3]}) # JsonResponse
        return render_template('login.html')
    user = request.form.get('user')
    pwd = request.form.get('pwd')
    if user == 'changxin' and pwd == "dsb":
        return redirect('/index')
    error = '用户名或密码错误'
    # return render_template('login.html',**{'error':error})
    return render_template('login.html',error=error)

@app.route('/index',endpoint='idx')
def index():
    data_dict = DATA_DICT
    return render_template('index.html',data_dict=data_dict)

@app.route('/edit',methods=['GET','POST'])
def edit():
    nid = request.args.get('nid')
    nid = int(nid)

    if request.method == "GET":
        info = DATA_DICT[nid]
        return render_template('edit.html',info=info)

    user = request.form.get('user')
    age = request.form.get('age')
    DATA_DICT[nid]['name'] = user
    DATA_DICT[nid]['age'] = age
    return redirect(url_for('idx'))

@app.route('/del/<int:nid>')
def delete(nid):
    del DATA_DICT[nid]
    # return redirect('/index')
    return redirect(url_for("idx"))

if __name__ == '__main__':
    app.run()
```

### 总结

1. flask路由

   ```
   @app.route('/login',methods=['GET','POST'])
   def login():
   	pass
   ```

2. 路由的参数

   ```
   @app.route('/login',methods=['GET','POST'],endpoint="login")
   def login():
   	pass
   	
   # 注意：endpoint不能重名
   ```

3. 动态路由

   ```
   @app.route('/index')
   def login():
   	pass
   	
   @app.route('/index/<name>')
   def login(name):
   	pass
   	
   @app.route('/index/<int:nid>')
   def login(nid):
   	pass
   ```

4. 获取提交的数据

   ```
   from flask import request
   
   @app.route('/index')
   def login():
   	request.args # GET形式传递的参数
   	request.form # POST形式提交的参数
   ```

5. 返回数据

   ```
   @app.route('/index')
   def login():
   	return render_template('模板文件')
   	return jsonify()
   	reutrn redirect('/index/') # reutrn redirect(url_for('idx'))
   	return "...."
   ```

6. 模板处理

   ```
   
   {{ x }}
   {% for item in list %}
   	{{item}}
   {% endfor %}
   
   ```

   

### 1.4 保存用户会话信息





### 2. 蓝图（blue print)

构建业务功能可拆分的目录结构。

面试题：django的app和flask的蓝图有什么区别？



## 总结

1. flask和django的区别？
2. 其他
3. flask的session是保存在：加密的形式保存在浏览器的cookie上。
4. 装饰器相关
   - 编写装饰器，记得加functools
   - 多个装饰器的应用
5. 蓝图



## 作业

在pro_excel项目中添加功能。

- 登录，session保持会话，用户表要放在MySQL中。 

- CURD，对数据库中的，书籍（title,author,price）。

- 上传excel文件，把excel中的文件内容导入数据库（xlrd）。【不讲】

- 【可选】在flask源码中找以下返回值对象。

  ```python
  from werkzeug.serving import run_simple
  
  def func(environ, start_response):
      print('请求来了')
      # 填充的代码
      
  if __name__ == '__main__':
      run_simple('127.0.0.1', 5000, func)
  ```

  

  

  







































































































