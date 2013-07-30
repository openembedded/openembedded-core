import unittest
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasFeature("x11-base"):
            skipModule("target doesn't have x11 in IMAGE_FEATURES")


class XorgTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_xorg_running(self):
        (status, output) = self.target.run(oeRuntimeTest.pscmd + ' |  grep -v xinit | grep [X]org')
        self.assertEqual(status, 0, msg="Xorg does not appear to be running %s" % self.target.run(oeRuntimeTest.pscmd)[1])

    @skipUnlessPassed('test_ssh')
    def test_xorg_error(self):
        (status, output) = self.target.run('cat /var/log/Xorg.0.log | grep -v "(EE) error," | grep -v "PreInit" | grep -v "evdev:" | grep -v "glx" | grep "(EE)"')
        self.assertEqual(status, 1, msg="Errors in Xorg log: %s" % output)

