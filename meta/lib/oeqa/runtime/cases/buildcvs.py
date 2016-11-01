from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.oeid import OETestID
from oeqa.core.decorator.data import skipIfNotFeature

from oeqa.runtime.utils.targetbuildproject import TargetBuildProject

class BuildCvsTest(OERuntimeTestCase):

    @classmethod
    def setUpClass(cls):
        uri = 'http://ftp.gnu.org/non-gnu/cvs/source/feature/1.12.13'
        uri = '%s/cvs-1.12.13.tar.bz2' % uri
        cls.project = TargetBuildProject(cls.tc.target,
                                         uri,
                                         dl_dir = cls.tc.td['DL_DIR'])
        cls.project.download_archive()

    @classmethod
    def tearDownClass(cls):
        cls.project.clean()

    @OETestID(205)
    @skipIfNotFeature('tools-sdk',
                      'Test requires tools-sdk to be in IMAGE_FEATURES')
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    def test_cvs(self):
        self.assertEqual(self.project.run_configure(), 0,
                        msg="Running configure failed")

        self.assertEqual(self.project.run_make(), 0,
                        msg="Running make failed")

        self.assertEqual(self.project.run_install(), 0,
                        msg="Running make install failed")
