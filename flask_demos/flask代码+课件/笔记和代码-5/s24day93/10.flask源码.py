from flask import Flask,session
# 1.1
app = Flask(__name__,static_url_path='/xx')

# 1.2
app.config.from_object('xx.xx')

# 1.3
@app.before_request
def f1():
    pass

@app.before_request
def f2():
    pass

@app.after_request
def f3(response):
    return response

@app.after_request
def f4(response):
    return response

@app.before_first_request
def f5():
    print('f5')

@app.before_first_request
def f6():
    print('f6')


@app.route('/index')
def index():
    session['k1'] = 123
    return 'hello world'

if __name__ == '__main__':
    app.run()
    app.__call__
