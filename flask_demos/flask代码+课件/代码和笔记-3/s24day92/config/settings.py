
#全局模式
DB_HOST = '127.0.0.1'
DB_USER = 'root'
DB_PWE = '123'

try:
    from .localsettings import *
except ImportError:
    pass

#基于类的方式
class Dving(object):
    DB_HOST = '127.0.0.4'