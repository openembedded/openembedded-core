from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import get_bb_var
import oe.path
import glob
import os
import os.path

class LibOE(oeSelfTest):
    def test_copy_tree_special(self):
        """
        Summary:    oe.path.copytree() should copy files with special character
        Expected:   'test file with sp£c!al @nd spaces' should exist in
                    copy destination
        Product:    OE-Core
        Author:     Joshua Lock <joshua.g.lock@intel.com>
        """
        tmp_dir = get_bb_var('TMPDIR')
        testloc = oe.path.join(tmp_dir, 'liboetests')
        src = oe.path.join(testloc, 'src')
        dst = oe.path.join(testloc, 'dst')
        bb.utils.mkdirhier(testloc)
        bb.utils.mkdirhier(src)
        testfilename = 'test file with sp£c!al @nd spaces'

        # create the test file and copy it
        open(oe.path.join(src, testfilename), 'w+b').close()
        oe.path.copytree(src, dst)

        # ensure path exists in dest
        fileindst = os.path.isfile(oe.path.join(dst, testfilename))
        self.assertTrue(fileindst, "File with spaces doesn't exist in dst")

        oe.path.remove(testloc)
