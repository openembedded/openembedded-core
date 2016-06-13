from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

class Selftest(oeRuntimeTest):

    @skipUnlessPassed("test_ssh")
    def test_install_package(self):
        """
        Summary: Check basic package installation functionality.
        Expected: 1. Before the test socat must be installed using scp.
                  2. After the test socat must be unistalled using ssh.
                     This can't be checked in this test.
        Product: oe-core
        Author: Mariano Lopez <mariano.lopez@intel.com>
        """

        (status, output) = self.target.run("socat -V")
        self.assertEqual(status, 0, msg="socat is not installed")

    @skipUnlessPassed("test_install_package")
    def test_verify_unistall(self):
        """
        Summary: Check basic package installation functionality.
        Expected: 1. test_install_package must unistall socat.
                     This test is just to verify that.
        Product: oe-core
        Author: Mariano Lopez <mariano.lopez@intel.com>
        """

        (status, output) = self.target.run("socat -V")
        self.assertNotEqual(status, 0, msg="socat is still installed")
