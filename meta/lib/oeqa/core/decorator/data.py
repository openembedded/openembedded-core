# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

from oeqa.core.exception import OEQAMissingVariable

from . import OETestDecorator, registerDecorator

@registerDecorator
class skipIfDataVar(OETestDecorator):
    """
        Skip test based on value of a data store's variable.

        It will get the info of var from the data store and will
        check it against value; if are equal it will skip the test
        with msg as the reason.
    """

    attrs = ('var', 'value', 'msg')

    def setUpDecorator(self):
        msg = 'Checking if %r value is %r to skip test' % (self.var, self.value)
        self.logger.debug(msg)
        if self.case.td.get(self.var) == self.value:
            self.case.skipTest(self.msg)

@registerDecorator
class OETestDataDepends(OETestDecorator):
    attrs = ('td_depends',)

    def setUpDecorator(self):
        for v in self.td_depends:
            try:
                value = self.case.td[v]
            except KeyError:
                raise OEQAMissingVariable("Test case need %s variable but"\
                        " isn't into td" % v)
