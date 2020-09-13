from flask import Blueprint

xwy = Blueprint('wy',__name__)

@xwy.route('/f3')
def f1():
    return 'xwyf3'

@xwy.route('/f4')
def f2():
    return 'xwyf4'
