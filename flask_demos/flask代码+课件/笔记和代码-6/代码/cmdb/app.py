from flask import Flask
import requests

app = Flask(__name__)

@app.route('/func')
def func():
    requests.get('http://www.baidu.com')
    return 'xxxx'

if __name__ == '__main__':
    app.run()
    app.__call__