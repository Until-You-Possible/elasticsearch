# @Time:2022/8/31 15:50
# @Author:Ray
# @File:JavaRunPythonWithParams.py
import sys

class TestPy:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    #
    # def func(self, b, c):
    #     return self + b + c



if __name__ == '__main__':
    # arr = []
    # for i in range(1, len(sys.argv)):
    #     arr.append((int(sys.argv[i])))
    dog = TestPy("pip", 3)
    print(dog)