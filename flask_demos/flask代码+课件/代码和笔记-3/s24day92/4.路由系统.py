from flask import Flask,render_template

app = Flask(__name__,)



def index():
    return render_template('index.html')
app.add_url_rule('/index', 'index', index)

@app.route('/login')
def login():
    return render_template('login.html')

if __name__ == '__main__':
    app.run()
    app.__call__