class Foo:
    def __init__(self):
        self.name = 'alex'
        self.__age = 123


obj = Foo()

print(obj.name)
print(obj._Foo__age)