from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.oeid import OETestID
from oeqa.core.decorator.data import skipIfNotFeature

from oeqa.runtime.utils.targetbuildproject import TargetBuildProject

class BuildIptablesTest(OERuntimeTestCase):

    @classmethod
    def setUpClass(cls):
        uri = 'http://downloads.yoctoproject.org/mirror/sources'
        uri = '%s/iptables-1.4.13.tar.bz2' % uri
        cls.project = TargetBuildProject(cls.tc.target,
                                         uri,
                                         dl_dir = cls.tc.td['DL_DIR'])
        cls.project.download_archive()

    @classmethod
    def tearDownClass(cls):
        cls.project.clean()

    @OETestID(206)
    @skipIfNotFeature('tools-sdk',
                      'Test requires tools-sdk to be in IMAGE_FEATURES')
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    def test_iptables(self):
        self.project.run_configure()
        self.project.run_make()
        self.project.run_install()

    @classmethod
    def tearDownClass(self):
        self.project.clean()
