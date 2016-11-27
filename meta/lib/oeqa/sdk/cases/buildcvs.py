from oeqa.sdk.case import OESDKTestCase
from oeqa.sdk.utils.sdkbuildproject import SDKBuildProject

class BuildCvsTest(OESDKTestCase):
    td_vars = ['TEST_LOG_DIR', 'DATETIME']

    @classmethod
    def setUpClass(self):
        dl_dir = self.td.get('DL_DIR', None)

        self.project = SDKBuildProject(self.tc.sdk_dir + "/cvs/", self.tc.sdk_env,
                        "http://ftp.gnu.org/non-gnu/cvs/source/feature/1.12.13/cvs-1.12.13.tar.bz2",
                        self.td['TEST_LOG_DIR'], self.td['DATETIME'], dl_dir=dl_dir)
        self.project.download_archive()

    def test_cvs(self):
        self.assertEqual(self.project.run_configure(), 0,
                        msg="Running configure failed")

        self.assertEqual(self.project.run_make(), 0,
                        msg="Running make failed")

        self.assertEqual(self.project.run_install(), 0,
                        msg="Running make install failed")

    @classmethod
    def tearDownClass(self):
        self.project.clean()
