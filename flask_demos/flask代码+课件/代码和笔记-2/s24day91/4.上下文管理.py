# class Foo(object):
#
#     def __enter__(self):
#         return 123
#
#     def __exit__(self, exc_type, exc_val, exc_tb):
#         pass
#
# obj = Foo()
#
# with obj as f:
#     print(f)

class Foo(object):

    def do_somthing(self):
        pass

    def close(self):
        pass

class Context:
    def __enter__(self):
        self.data = Foo()
        return self.data

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.data.close()

with Context() as ctx:
    ctx.do_somthing()
