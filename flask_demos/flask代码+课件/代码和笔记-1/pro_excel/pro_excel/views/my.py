from flask import Blueprint

xmy = Blueprint('my',__name__)

@xmy.route('/f1')
def f1():
    return 'f1'

@xmy.route('/f2')
def f2():
    return 'f2'

