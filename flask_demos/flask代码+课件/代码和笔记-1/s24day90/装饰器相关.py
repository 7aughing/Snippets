

# @app.route('xxx') # endpoint默认等于login
# def login():
#     pass

"""
import functools
def auth(func):
    @functools.wraps(func)
    def inner(*args,**kwargs):
        return func(*args,**kwargs)
    return inner

@auth
def login():
    pass

@auth
def index():
    pass
print(login.__name__)
print(index.__name__)
"""

"""
import functools

def auth0(func):
    print('0')
    @functools.wraps(func)
    def inner(*args,**kwargs):
        return func(*args,**kwargs)
    return inner

def auth1(func):
    @functools.wraps(func)
    def inner(*args,**kwargs):
        return func(*args,**kwargs)
    return inner

@auth0
@auth1
def index():
    print('index')
"""











