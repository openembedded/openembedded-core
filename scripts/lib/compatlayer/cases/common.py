# Copyright (C) 2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import glob
import os
import unittest
from compatlayer import get_signatures, LayerType, check_command, get_depgraph
from compatlayer.case import OECompatLayerTestCase

class CommonCompatLayer(OECompatLayerTestCase):
    def test_readme(self):
        # The top-level README file may have a suffix (like README.rst or README.txt).
        readme_files = glob.glob(os.path.join(self.tc.layer['path'], 'README*'))
        self.assertTrue(len(readme_files) > 0,
                        msg="Layer doesn't contains README file.")

        # There might be more than one file matching the file pattern above
        # (for example, README.rst and README-COPYING.rst). The one with the shortest
        # name is considered the "main" one.
        readme_file = sorted(readme_files)[0]
        data = ''
        with open(readme_file, 'r') as f:
            data = f.read()
        self.assertTrue(data,
                msg="Layer contains a README file but it is empty.")

    def test_parse(self):
        check_command('Layer %s failed to parse.' % self.tc.layer['name'],
                      'bitbake -p')

    def test_show_environment(self):
        check_command('Layer %s failed to show environment.' % self.tc.layer['name'],
                      'bitbake -e')

    def test_world(self):
        '''
        "bitbake world" is expected to work. test_signatures does not cover that
        because it is more lenient and ignores recipes in a world build that
        are not actually buildable, so here we fail when "bitbake -S none world"
        fails.
        '''
        get_signatures(self.td['builddir'], failsafe=False)

    def test_signatures(self):
        if self.tc.layer['type'] == LayerType.SOFTWARE and \
           not self.tc.test_software_layer_signatures:
            raise unittest.SkipTest("Not testing for signature changes in a software layer %s." \
                     % self.tc.layer['name'])

        # task -> (old signature, new signature)
        sig_diff = {}
        curr_sigs, _ = get_signatures(self.td['builddir'], failsafe=True)
        for task in self.td['sigs']:
            if task in curr_sigs and \
               self.td['sigs'][task] != curr_sigs[task]:
                sig_diff[task] = (self.td['sigs'][task], curr_sigs[task])

        if sig_diff:
            # Beware, depgraph uses task=<pn>.<taskname> whereas get_signatures()
            # uses <pn>:<taskname>. Need to convert sometimes. The output follows
            # the convention from get_signatures() because that seems closer to
            # normal bitbake output.
            def sig2graph(task):
                pn, taskname = task.rsplit(':', 1)
                return pn + '.' + taskname
            def graph2sig(task):
                pn, taskname = task.rsplit('.', 1)
                return pn + ':' + taskname
            depgraph = get_depgraph(failsafe=True)
            depends = depgraph['tdepends']

            # If a task A has a changed signature, but none of its
            # dependencies, then we need to report it because it is
            # the one which introduces a change. Any task depending on
            # A (directly or indirectly) will also have a changed
            # signature, but we don't need to report it. It might have
            # its own changes, which will become apparent once the
            # issues that we do report are fixed and the test gets run
            # again.
            sig_diff_filtered = []
            for task, (old_sig, new_sig) in sig_diff.items():
                deps_tainted = False
                for dep in depends.get(sig2graph(task), ()):
                    if graph2sig(dep) in sig_diff:
                        deps_tainted = True
                        break
                if not deps_tainted:
                    sig_diff_filtered.append((task, old_sig, new_sig))

            msg = []
            msg.append('Layer %s changed %d signatures, initial differences (first hash without, second with layer):' %
                       (self.tc.layer['name'], len(sig_diff)))
            for diff in sorted(sig_diff_filtered):
                recipe, taskname = diff[0].rsplit(':', 1)
                cmd = 'bitbake-diffsigs --task %s %s --signature %s %s' % \
                      (recipe, taskname, diff[1], diff[2])
                msg.append('   %s: %s -> %s' % diff)
                msg.append('      %s' % cmd)
                try:
                    output = check_command('Determining signature difference failed.',
                                           cmd).decode('utf-8')
                except RuntimeError as error:
                    output = str(error)
                if output:
                    msg.extend(['      ' + line for line in output.splitlines()])
                    msg.append('')
            self.fail('\n'.join(msg))
