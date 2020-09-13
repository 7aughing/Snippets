import pymysql
from flask import Flask

app = Flask(__name__)

CONN = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='123', db='t1')

def fetchall(sql):
    cursor = CONN.cursor()
    cursor.execute(sql)
    result = cursor.fetchall()
    cursor.close()
    return result

@app.route('/login')
def login():
    result = fetchall('select * from user')
    return 'login'


@app.route('/index')
def index():
    result = fetchall('select * from user')
    return 'xxx'


@app.route('/order')
def order():
    result = fetchall('select * from user')
    return 'xxx'


if __name__ == '__main__':
    app.run()