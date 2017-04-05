# Copyright (C) 2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
import unittest
from compatlayer import get_signatures, LayerType, check_command
from compatlayer.case import OECompatLayerTestCase

class CommonCompatLayer(OECompatLayerTestCase):
    def test_readme(self):
        readme_file = os.path.join(self.tc.layer['path'], 'README')
        self.assertTrue(os.path.isfile(readme_file),
                msg="Layer doesn't contains README file.")

        data = ''
        with open(readme_file, 'r') as f:
            data = f.read()
        self.assertTrue(data,
                msg="Layer contains README file but is empty.")

    def test_parse(self):
        check_command('Layer %s failed to parse.' % self.tc.layer['name'],
                      'bitbake -p')

    def test_show_environment(self):
        check_command('Layer %s failed to show environment.' % self.tc.layer['name'],
                      'bitbake -e')

    def test_signatures(self):
        if self.tc.layer['type'] == LayerType.SOFTWARE:
            raise unittest.SkipTest("Layer %s isn't BSP or DISTRO one." \
                     % self.tc.layer['name'])

        sig_diff = {}

        curr_sigs = get_signatures(self.td['builddir'], failsafe=True)
        for task in self.td['sigs']:
            if task not in curr_sigs:
                continue

            if self.td['sigs'][task] != curr_sigs[task]:
                sig_diff[task] = '%s -> %s' % \
                        (self.td['sigs'][task], curr_sigs[task])

        detail = ''
        if sig_diff:
            for task in sig_diff:
                detail += "%s changed %s\n" % (task, sig_diff[task])
        self.assertFalse(bool(sig_diff), "Layer %s changed signatures.\n%s" % \
                (self.tc.layer['name'], detail))

