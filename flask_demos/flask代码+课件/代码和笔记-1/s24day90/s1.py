from flask import Flask
import functools

app = Flask(__name__)

def auth(func):
    @functools.wraps(func)
    def inner(*args,**kwargs):
        return func(*args,**kwargs)
    return inner

@app.route('/index')
@auth
def index():
    return 'hello world'

@app.route('/login')
@auth
def login():
    return 'hello world'


if __name__ == '__main__':
    app.run()