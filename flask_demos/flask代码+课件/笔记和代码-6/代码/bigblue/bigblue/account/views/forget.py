from .. import account
from flask import render_template

@account.route('/login')
def login():
    return render_template('login.html')