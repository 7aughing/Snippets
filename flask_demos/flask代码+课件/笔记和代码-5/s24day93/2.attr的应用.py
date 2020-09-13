# class Foo:
#     pass
#
# obj = Foo()
# obj.x1 = 123
# print(obj.x1)
#


class Local(object):
    def __init__(self):
        # self.storage = {}
        object.__setattr__(self,"storage",{})

    def __setattr__(self, key, value):
        self.storage[key] = value

    def __getattr__(self, item):
        return self.storage.get(item)

local = Local()
# local.x1 = 123
# print(local.x1)