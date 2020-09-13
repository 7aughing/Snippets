from flask import Flask,render_template

app = Flask(__name__,template_folder='templates',static_folder='static',static_url_path='/16554654')

"""
    1.执行 f = app.route('/index')
    2. 
        @decorator
        def index():
            pass
"""

# def index():
#     return render_template('index.html')
# app.add_url_rule('/index', 'index', index)

# @app.route('/login')
# def login():
#     return render_template('login.html')

@app.route('/index')
def index():
    return render_template('index.html')

if __name__ == '__main__':
    app.run()