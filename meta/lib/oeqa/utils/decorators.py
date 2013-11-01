# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Some custom decorators that can be used by unittests
# Most useful is skipUnlessPassed which can be used for
# creating dependecies between two test methods.

from oeqa.oetest import *

class skipIfFailure(object):

    def __init__(self,testcase):
        self.testcase = testcase

    def __call__(self,f):
        def wrapped_f(*args):
            if self.testcase in (oeTest.testFailures or oeTest.testErrors):
                raise unittest.SkipTest("Testcase dependency not met: %s" % self.testcase)
            return f(*args)
        wrapped_f.__name__ = f.__name__
        return wrapped_f

class skipIfSkipped(object):

    def __init__(self,testcase):
        self.testcase = testcase

    def __call__(self,f):
        def wrapped_f(*args):
            if self.testcase in oeTest.testSkipped:
                raise unittest.SkipTest("Testcase dependency not met: %s" % self.testcase)
            return f(*args)
        wrapped_f.__name__ = f.__name__
        return wrapped_f

class skipUnlessPassed(object):

    def __init__(self,testcase):
        self.testcase = testcase

    def __call__(self,f):
        def wrapped_f(*args):
            if self.testcase in oeTest.testSkipped or \
                    self.testcase in  oeTest.testFailures or \
                    self.testcase in oeTest.testErrors:
                raise unittest.SkipTest("Testcase dependency not met: %s" % self.testcase)
            return f(*args)
        wrapped_f.__name__ = f.__name__
        return wrapped_f
