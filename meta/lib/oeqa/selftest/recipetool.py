import unittest
import os
import logging
import re
import tempfile

import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var
from oeqa.utils.decorators import testcase
from oeqa.selftest.devtool import DevtoolBase


class RecipetoolTests(DevtoolBase):

    def setUpLocal(self):
        self.tempdir = tempfile.mkdtemp(prefix='recipetoolqa')
        self.track_for_cleanup(self.tempdir)

    def test_recipetool_create(self):
        # Try adding a recipe
        tempsrc = os.path.join(self.tempdir, 'srctree')
        os.makedirs(tempsrc)
        recipefile = os.path.join(self.tempdir, 'logrotate_3.8.7.bb')
        srcuri = 'https://fedorahosted.org/releases/l/o/logrotate/logrotate-3.8.7.tar.gz'
        result = runCmd('recipetool create -o %s %s -x %s' % (recipefile, srcuri, tempsrc))
        self.assertTrue(os.path.isfile(recipefile))
        checkvars = {}
        checkvars['LICENSE'] = 'GPLv2'
        checkvars['LIC_FILES_CHKSUM'] = 'file://COPYING;md5=18810669f13b87348459e611d31ab760'
        checkvars['SRC_URI'] = 'https://fedorahosted.org/releases/l/o/logrotate/logrotate-${PV}.tar.gz'
        checkvars['SRC_URI[md5sum]'] = '99e08503ef24c3e2e3ff74cc5f3be213'
        checkvars['SRC_URI[sha256sum]'] = 'f6ba691f40e30e640efa2752c1f9499a3f9738257660994de70a45fe00d12b64'
        self._test_recipe_contents(recipefile, checkvars, [])

    def test_recipetool_create_git(self):
        # Ensure we have the right data in shlibs/pkgdata
        bitbake('libpng pango libx11 libxext jpeg')
        # Try adding a recipe
        tempsrc = os.path.join(self.tempdir, 'srctree')
        os.makedirs(tempsrc)
        recipefile = os.path.join(self.tempdir, 'libmatchbox.bb')
        srcuri = 'git://git.yoctoproject.org/libmatchbox'
        result = runCmd('recipetool create -o %s %s -x %s' % (recipefile, srcuri, tempsrc))
        self.assertTrue(os.path.isfile(recipefile), 'recipetool did not create recipe file; output:\n%s' % result.output)
        checkvars = {}
        checkvars['LICENSE'] = 'LGPLv2.1'
        checkvars['LIC_FILES_CHKSUM'] = 'file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34'
        checkvars['S'] = '${WORKDIR}/git'
        checkvars['PV'] = '1.0+git${SRCPV}'
        checkvars['SRC_URI'] = srcuri
        checkvars['DEPENDS'] = 'libpng pango libx11 libxext jpeg'
        inherits = ['autotools', 'pkgconfig']
        self._test_recipe_contents(recipefile, checkvars, inherits)

