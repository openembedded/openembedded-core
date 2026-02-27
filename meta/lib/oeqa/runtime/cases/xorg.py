#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.data import skipIfNotFeature
from oeqa.runtime.decorator.package import OEHasPackage

class XorgTest(OERuntimeTestCase):

    @skipIfNotFeature('x11-base',
                      'Test requires x11 to be in IMAGE_FEATURES')
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['xserver-nodm-init'])
    def test_xorg_running(self):
        cmd ='%s | grep -v xinit | grep [X]org' % self.tc.target_cmds['ps']
        status, output = self.target.run(cmd)
        msg = ('Xorg does not appear to be running %s' %
              self.target.run(self.tc.target_cmds['ps'])[1])

        # dump last 20 lines of emptty log in case of failure
        log_cmd = 'tail -n 20 /var/log/emptty/7.log'
        msg += '\n\n===== start: snippet =====\n\n'
        msg += 'file: /var/log/emptty/7.log\n\n'
        msg += '\n\n%s\n\n' % self.target.run(log_cmd)[1]
        msg += '\n\n===== end: snippet =====\n\n'

        self.assertEqual(status, 0, msg=msg)
