import os
import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var
from oeqa.utils.decorators import testcase

class KernelTests(oeSelfTest):
    def  test_dummy_kernel(self):
        """
        [YP#7202]
        - test that linux-dummy target can be used as kernel provider for an image
        - check no "multiple providers are available for" message is received while building the image
        """
        config_param = 'PREFERRED_PROVIDER_virtual/kernel = "linux-dummy"'
        self.append_config(config_param)
        arch_dir = get_bb_var('MULTIMACH_TARGET_SYS', target='linux-dummy')
        stamps_dir = os.path.join(os.getenv('BUILDDIR'), "tmp/stamps")
        lnx_dmy_stamps_dir = os.path.join(stamps_dir, arch_dir, 'linux-dummy')
        res = bitbake("linux-dummy -ccleansstate") # ensure we have nothing related to linux-dummy in stamps dir.
        self.assertFalse(os.listdir(lnx_dmy_stamps_dir), msg='linux-dummy stamps dir. should have been cleaned. Something \
                         happened with bitbake linux-dummy -ccleansstate')
        res = bitbake("core-image-minimal")# testing linux-dummy is both buildable and usable within an image
        self.remove_config(config_param)
        self.assertEqual(res.status, 0, msg="core-image-minimal failed to build. Please check logs. ")
        self.assertNotIn("multiple providers are available for", res.output, msg="'multiple providers are available for\
                        linux-dummy' message received during buildtime.")
        self.assertTrue(os.listdir(lnx_dmy_stamps_dir), msg="linux-dummy didn't build correctly. No stamp present in stamps \
                        dir. %s" % lnx_dmy_stamps_dir)
         
