import unittest
import os
import logging
import re
import shutil

import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd
from oeqa.utils.decorators import testcase

class BitbakeLayers(oeSelfTest):

    @testcase(756)
    def test_bitbakelayers_showcrossdepends(self):
        result = runCmd('bitbake-layers show-cross-depends')
        self.assertTrue('aspell' in result.output)

    @testcase(83)
    def test_bitbakelayers_showlayers(self):
        result = runCmd('bitbake-layers show_layers')
        self.assertTrue('meta-selftest' in result.output)

    @testcase(93)
    def test_bitbakelayers_showappends(self):
        result = runCmd('bitbake-layers show_appends')
        self.assertTrue('xcursor-transparent-theme_0.1.1.bbappend' in result.output, msg='xcursor-transparent-theme_0.1.1.bbappend file was not recognised')

    @testcase(90)
    def test_bitbakelayers_showoverlayed(self):
        result = runCmd('bitbake-layers show_overlayed')
        self.assertTrue('aspell' in result.output, msg='xcursor-transparent-theme_0.1.1.bbappend file was not recognised')

    @testcase(95)
    def test_bitbakelayers_flatten(self):
        self.assertFalse(os.path.isdir(os.path.join(self.builddir, 'test')))
        result = runCmd('bitbake-layers flatten test')
        bb_file = os.path.join(self.builddir, 'test/recipes-graphics/xcursor-transparent-theme/xcursor-transparent-theme_0.1.1.bb')
        self.assertTrue(os.path.isfile(bb_file))
        contents = ftools.read_file(bb_file)
        find_in_contents = re.search("##### bbappended from meta-selftest #####\n(.*\n)*include test_recipe.inc", contents)
        shutil.rmtree(os.path.join(self.builddir, 'test'))
        self.assertTrue(find_in_contents)
