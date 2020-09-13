
class LocalProxy(object):
    def __init__(self,xxx):
        self.xxx = xxx() # ctx.session   / ctx.reqeust

    def __setitem__(self, key, value):
        self.xxx[key] = value # ctx.session[key] = value

    def __getitem__(self, item):
        return self.xxx[item] # ctx.session[item]

    def __getattr__(self, item):
        return getattr(self.xxx,item) # ctx.request.method

def func():
    return ctx.session

x1 = LocalProxy(func)
x1['k1'] = 123
x1['k8']

def function():
    return ctx.request
x2 = LocalProxy(function)
print(x2.method)