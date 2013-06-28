from oeqa.oetest import *

class skipIfFailure(object):

    def __init__(self,testcase):
        self.testcase = testcase

    def __call__(self,f):
        def wrapped_f(*args):
            if self.testcase in (oeRuntimeTest.testFailures or oeRuntimeTest.testErrors):
                raise unittest.SkipTest("Testcase dependency not met: %s" % self.testcase)
            f(*args)
        wrapped_f.__name__ = f.__name__
        return wrapped_f

class skipIfSkipped(object):

    def __init__(self,testcase):
        self.testcase = testcase

    def __call__(self,f):
        def wrapped_f(*args):
            if self.testcase in oeRuntimeTest.testSkipped:
                raise unittest.SkipTest("Testcase dependency not met: %s" % self.testcase)
            f(*args)
        wrapped_f.__name__ = f.__name__
        return wrapped_f

class skipUnlessPassed(object):

    def __init__(self,testcase):
        self.testcase = testcase

    def __call__(self,f):
        def wrapped_f(*args):
            if self.testcase in (oeRuntimeTest.testSkipped, oeRuntimeTest.testFailures, oeRuntimeTest.testErrors):
                raise unittest.SkipTest("Testcase dependency not met: %s" % self.testcase)
            f(*args)
        wrapped_f.__name__ = f.__name__
        return wrapped_f
