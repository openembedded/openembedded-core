from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *
import re

class DateTest(oeRuntimeTest):

    @testcase(211)
    @skipUnlessPassed("test_ssh")
    def test_date(self):
        (status, output) = self.target.run('date +"%Y-%m-%d %T"')
        self.assertEqual(status, 0, msg="Failed to get initial date, output: %s" % output)
        oldDate = output

        sampleDate = '"2016-08-09 10:00:00"'
        (status, output) = self.target.run("date -s %s" % sampleDate)
        self.assertEqual(status, 0, msg="Date set failed, output: %s" % output)

        (status, output) = self.target.run("date -R")
        p = re.match('Tue, 09 Aug 2016 10:00:.. \+0000', output)
        self.assertTrue(p, msg="The date was not set correctly, output: %s" % output)

        (status, output) = self.target.run('date -s "%s"' % oldDate)
        self.assertEqual(status, 0, msg="Failed to reset date, output: %s" % output)
