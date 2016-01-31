import shutil

from oeqa.oetest import oeSDKExtTest
from oeqa.utils.decorators import *

class DevtoolTest(oeSDKExtTest):

    @classmethod
    def setUpClass(self):
        self.myapp_src = os.path.join(self.tc.sdkextfilesdir, "myapp")
        self.myapp_dst = os.path.join(self.tc.sdktestdir, "myapp")
        shutil.copytree(self.myapp_src, self.myapp_dst)

    def test_devtool_add_reset(self):
        self._run('devtool add myapp %s' % self.myapp_dst)
        self._run('devtool reset myapp')

    def test_devtool_build(self):
        self._run('devtool add myapp %s' % self.myapp_dst)
        self._run('devtool build myapp')
        self._run('devtool reset myapp')

    @classmethod
    def tearDownClass(self):
        shutil.rmtree(self.myapp_dst)
