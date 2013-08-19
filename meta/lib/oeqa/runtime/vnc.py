from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *
import re

def setUpModule():
    skipModuleUnless(oeRuntimeTest.tc.target.run('which x11vnc')[0] == 0, "No x11vnc in image")

class VNCTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_vnc(self):
        (status, output) = self.target.run('x11vnc -display :0.0 -bg -q')
        self.assertEqual(status, 0, msg="x11vnc server failed to start: %s" % output)
        port = re.search('PORT=[0-9]*', output)
        self.assertTrue(port, msg="Listening port not specified in command output: %s" %output)

        (status, output) = self.target.run(oeRuntimeTest.pscmd + ' | grep -i [x]11vnc')
        self.assertEqual(status, 0, msg="x11vnc process not running")

        vncport = port.group(0).split('=')[1]
        (status, output) = self.target.run('netstat -atun | grep :%s | grep LISTEN' % vncport)
        self.assertEqual(status, 0, msg="x11vnc server not running on port %s" % vncport)
