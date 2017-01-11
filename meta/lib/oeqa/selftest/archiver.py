from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import bitbake, get_bb_var
from oeqa.utils.decorators import testcase
import glob
import os
import shutil


class Archiver(oeSelfTest):

    @testcase(1345)
    def test_archiver_allows_to_filter_on_recipe_name(self):
        """
        Summary:     The archiver should offer the possibility to filter on the recipe. (#6929)
        Expected:    1. Included recipe (busybox) should be included
                     2. Excluded recipe (zlib) should be excluded
        Product:     oe-core
        Author:      Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        include_recipe = 'busybox'
        exclude_recipe = 'zlib'

        features = 'INHERIT += "archiver"\n'
        features += 'ARCHIVER_MODE[src] = "original"\n'
        features += 'COPYLEFT_PN_INCLUDE = "%s"\n' % include_recipe
        features += 'COPYLEFT_PN_EXCLUDE = "%s"\n' % exclude_recipe
        self.write_config(features)

        shutil.rmtree(get_bb_var('TMPDIR'))
        bitbake("%s %s" % (include_recipe, exclude_recipe))

        src_path = os.path.join(get_bb_var('DEPLOY_DIR_SRC'), get_bb_var('TARGET_SYS'))

        # Check that include_recipe was included
        included_present = len(glob.glob(src_path + '/%s-*' % include_recipe))
        self.assertTrue(included_present, 'Recipe %s was not included.' % include_recipe)

        # Check that exclude_recipe was excluded
        excluded_present = len(glob.glob(src_path + '/%s-*' % exclude_recipe))
        self.assertFalse(excluded_present, 'Recipe %s was not excluded.' % exclude_recipe)
