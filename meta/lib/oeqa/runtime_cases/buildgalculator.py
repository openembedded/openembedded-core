from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *
from oeqa.runtime.utils.targetbuildproject import TargetBuildProject

def setUpModule():
    if not oeRuntimeTest.hasFeature("tools-sdk"):
        skipModule("Image doesn't have tools-sdk in IMAGE_FEATURES")

class GalculatorTest(oeRuntimeTest):
    @testcase(1526)
    @skipUnlessPassed("test_ssh")
    def test_galculator(self):
        dl_dir = oeRuntimeTest.tc.d.getVar('DL_DIR', True)
        try:
            project = TargetBuildProject(oeRuntimeTest.tc.target,
                                      "http://galculator.mnim.org/downloads/galculator-2.1.4.tar.bz2",
                                      dl_dir=dl_dir)
            project.download_archive()

            self.assertEqual(project.run_configure(), 0,
                            msg="Running configure failed")

            self.assertEqual(project.run_make(), 0,
                            msg="Running make failed")
        finally:
            project.clean()
