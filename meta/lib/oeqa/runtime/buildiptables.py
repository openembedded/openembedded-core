from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *
from oeqa.runtime.utils.targetbuildproject import TargetBuildProject

def setUpModule():
    if not oeRuntimeTest.hasFeature("tools-sdk"):
        skipModule("Image doesn't have tools-sdk in IMAGE_FEATURES")

class BuildIptablesTest(oeRuntimeTest):

    @classmethod
    def setUpClass(self):
        dl_dir = oeRuntimeTest.tc.d.getVar('DL_DIR', True)
        self.project = TargetBuildProject(oeRuntimeTest.tc.target,
                        "http://downloads.yoctoproject.org/mirror/sources/iptables-1.4.13.tar.bz2",
                        dl_dir=dl_dir)
        self.project.download_archive()

    @testcase(206)
    @skipUnlessPassed("test_ssh")
    def test_iptables(self):
        self.assertEqual(self.project.run_configure(), 0,
                        msg="Running configure failed")

        self.assertEqual(self.project.run_make(), 0,
                        msg="Running make failed")

        self.assertEqual(self.project.run_install(), 0,
                        msg="Running make install failed")

    @classmethod
    def tearDownClass(self):
        self.project.clean()
