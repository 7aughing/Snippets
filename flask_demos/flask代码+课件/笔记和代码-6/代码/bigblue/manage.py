from flask import Flask, render_template

# 生成app对象
app = Flask(__name__)


@app.route('/index')
def index():
    return render_template('index.html')


@app.route('/order')
def order():
    return render_template('order.html')


class MyMiddleware(object):
    def __init__(self, old_app):
        # app.wsgi_app给了 self.wsgi_app
        self.wsgi_app = old_app.wsgi_app

    def __call__(self, *args, **kwargs):
        print('123')
        result = self.wsgi_app(*args, **kwargs)
        print('456')
        return result
    # 将app代入MyMiddleware中 赋值给app.wsgi_app


app.wsgi_app = MyMiddleware(app)

if __name__ == '__main__':
    app.run()
    app.__call__