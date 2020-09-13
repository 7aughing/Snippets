class Foo(object):

    def __setattr__(self, key, value):
        print(key,value)

    def __getattr__(self, item):
        print(item)

obj = Foo()
obj.x = 123
obj.x

from rest_framework.request import Request