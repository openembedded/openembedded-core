#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import os

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, get_bb_var
import oeqa.utils.ftools as ftools

class LayerAppendTests(OESelftestTestCase):
    # Each layer depends on the previous one so that the bbappends are
    # applied in a defined order (layer 2's after layer 1's)
    layerconf = """
LAYERNAME = "meta-layertestINT"
LAYERDEPENDS = "DEPENDS"
LAYERSERIES_COMPAT = "CORENAMES"
"""
    recipe = """
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
INHIBIT_DEFAULT_DEPS = "1"

python do_build() {
    bb.plain('Building ...')
}
addtask build
"""
    append = """
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://appendtest.txt"

sysroot_stage_all:append() {
	install -m 644 ${UNPACKDIR}/appendtest.txt ${SYSROOT_DESTDIR}/
}

"""

    append2 = """
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://appendtest.txt"
"""
    layerappend = ''

    def tearDownLocal(self):
        if self.layerappend:
            ftools.remove_from_file(self.builddir + "/conf/bblayers.conf", self.layerappend)
        super().tearDownLocal()

    def test_layer_appends(self):
        corebase = get_bb_var("COREBASE")
        corenames = get_bb_var("LAYERSERIES_CORENAMES")
        depends = {"0": "oe-core", "1": "meta-layertest0", "2": "meta-layertest1"}

        for l in ["0", "1", "2"]:
            layer = os.path.join(corebase, "meta-layertest" + l)
            self.assertFalse(os.path.exists(layer))
            os.mkdir(layer)
            os.mkdir(layer + "/conf")
            with open(layer + "/conf/layer.conf", "w") as f:
                f.write(self.layerconf.replace("INT", l).replace("DEPENDS", depends[l]).replace("CORENAMES", corenames))
            os.mkdir(layer + "/recipes-test")
            if l == "0":
                with open(layer + "/recipes-test/layerappendtest.bb", "w") as f:
                    f.write(self.recipe)
            elif l == "1":
                with open(layer + "/recipes-test/layerappendtest.bbappend", "w") as f:
                    f.write(self.append)
                os.mkdir(layer + "/recipes-test/layerappendtest")
                with open(layer + "/recipes-test/layerappendtest/appendtest.txt", "w") as f:
                    f.write("Layer 1 test")
            elif l == "2":
                with open(layer + "/recipes-test/layerappendtest.bbappend", "w") as f:
                    f.write(self.append2)
                os.mkdir(layer + "/recipes-test/layerappendtest")
                with open(layer + "/recipes-test/layerappendtest/appendtest.txt", "w") as f:
                    f.write("Layer 2 test")
            self.track_for_cleanup(layer)

        self.layerappend = "BBLAYERS += \"{0}/meta-layertest0 {0}/meta-layertest1 {0}/meta-layertest2\"".format(corebase)
        ftools.append_file(self.builddir + "/conf/bblayers.conf", self.layerappend)
        stagingdir = get_bb_var("SYSROOT_DESTDIR", "layerappendtest")
        bitbake("layerappendtest")
        data = ftools.read_file(stagingdir + "/appendtest.txt")
        self.assertEqual(data, "Layer 2 test")
        os.remove(corebase + "/meta-layertest2/recipes-test/layerappendtest/appendtest.txt")
        bitbake("layerappendtest")
        data = ftools.read_file(stagingdir + "/appendtest.txt")
        self.assertEqual(data, "Layer 1 test")
        with open(corebase + "/meta-layertest2/recipes-test/layerappendtest/appendtest.txt", "w") as f:
            f.write("Layer 2 test")
        bitbake("layerappendtest")
        data = ftools.read_file(stagingdir + "/appendtest.txt")
        self.assertEqual(data, "Layer 2 test")
