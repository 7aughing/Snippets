from flask import Blueprint


x = Blueprint(__name__,'x')


@x.before_request
def f1():
    pass

@x.app_template_global()
def xxxxx():
    return 'xxx'

@x.route('/index')
def index():
    pass


@x.route('/index')
def index():
    pass

@x.route('/index')
def index():
    pass