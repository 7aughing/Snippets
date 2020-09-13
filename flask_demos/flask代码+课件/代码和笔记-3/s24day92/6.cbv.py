from flask import Flask,render_template,views

app = Flask(__name__,)

def test1(func):
    def inner(*args,**kwargs):
        print('before1')
        result = func(*args,**kwargs)
        print('after1')
        return result
    return inner

def test2(func):
    def inner(*args,**kwargs):
        print('before2')
        result = func(*args,**kwargs)
        print('after2')
        return result
    return inner





# class UserView(views.MethodView):
#     methods = ['GET',"POST"]
#
#     decorators = [test1,test2]
#     def get(self):
#         print('get')
#         return 'get'
#
#     def post(self):
#         print('post')
#         return 'post'
#
# app.add_url_rule('/user', view_func=UserView.as_view('user')) # endpoint

class UserView(views.MethodView):
    methods = ['POST']
    decorators = [test1,test2]
    def get(self):
        print('get')
        return 'get'

    def post(self):
        print('post')
        return 'post'
app.add_url_rule('/user',view_func=UserView.as_view('user'))

if __name__ == '__main__':
    app.run()