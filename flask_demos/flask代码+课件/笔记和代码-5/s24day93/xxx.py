# v = dict()
# v['k1'] = 123 # 调用dict.__setitem__
# print(v)


class Config(dict):
    pass
v = Config()
v['k1'] = 123
print(v)

