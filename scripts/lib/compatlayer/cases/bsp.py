# Copyright (C) 2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import unittest

from compatlayer import LayerType
from compatlayer.case import OECompatLayerTestCase

class BSPCompatLayer(OECompatLayerTestCase):
    @classmethod
    def setUpClass(self):
        if self.tc.layer['type'] != LayerType.BSP:
            raise unittest.SkipTest("BSPCompatLayer: Layer %s isn't BSP one." %\
                self.tc.layer['name'])

    def test_bsp_defines_machines(self):
        self.assertTrue(self.tc.layer['conf']['machines'], 
                "Layer is BSP but doesn't defines machines.")

    def test_bsp_no_set_machine(self):
        from oeqa.utils.commands import get_bb_var

        machine = get_bb_var('MACHINE')
        self.assertEqual(self.td['bbvars']['MACHINE'], machine,
                msg="Layer %s modified machine %s -> %s" % \
                    (self.tc.layer['name'], self.td['bbvars']['MACHINE'], machine))
