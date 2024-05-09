#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import logging
import os
import sys

import bb.utils

from bblayers.common import LayerPlugin

logger = logging.getLogger('bitbake-config-layers')

sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))

def plugin_init(plugins):
    return ConfigFragmentsPlugin()

class ConfigFragmentsPlugin(LayerPlugin):
    def get_fragment_info(self, path):
        summary = ""
        description = []
        with open(path) as f:
            for l in f.readlines():
                if not l.startswith('#'):
                    break
                if not summary:
                    summary = l[1:].strip()
                else:
                    description.append(l[1:].strip())
        if not summary or not description:
            raise Exception('Please add a one-line summary followed by a description as #-prefixed comments at the beginning of {}'.format(path))

        return summary, description


    def discover_fragments(self):
        allfragments = {}
        for layername in self.bbfile_collections:
             layerdir = self.bbfile_collections[layername]
             fragments = []
             for topdir, dirs, files in os.walk(os.path.join(layerdir, 'conf/fragments')):
                 fragmentdir = topdir.split('conf/fragments/')[-1]
                 for fragmentfile in sorted(files):
                     fragmentname = "/".join((fragmentdir, fragmentfile.split('.')[0]))
                     fragmentpath = os.path.join(topdir, fragmentfile)
                     fragmentsummary, fragmentdesc = self.get_fragment_info(fragmentpath)
                     fragments.append({'path':fragmentpath, 'name':fragmentname, 'summary':fragmentsummary, 'description':fragmentdesc})
             if fragments:
                 allfragments[layername] = {'layerdir':layerdir,'fragments':fragments}
        return allfragments


    def do_list_fragments(self, args):
        """ List available configuration fragments """
        for layername, layerdata in self.discover_fragments().items():
            layerdir = layerdata['layerdir']
            fragments = layerdata['fragments']

            print('Available fragments in {} layer located in {}:\n'.format(layername, layerdir))
            for f in fragments:
                if not args.verbose:
                    print('{}\t{}'.format(f['name'], f['summary']))
                else:
                    print('Name: {}\nPath: {}\nSummary: {}\nDescription:\n{}\n'.format(f['name'], f['path'], f['summary'],''.join(f['description'])))
            print('')

    def fragment_exists(self, fragmentname):
        for layername, layerdata in self.discover_fragments().items():
            for f in layerdata['fragments']:
              if f['name'] == fragmentname:
                  return True
        return False

    def do_add_fragment(self, args):
        """ Add a fragment to the local build configuration """
        if not self.fragment_exists(args.fragmentname):
            raise Exception("Fragment {} does not exist; use 'list-fragments' to see the full list.".format(args.fragmentname))

        confpath = os.path.join(os.environ["BBPATH"], "conf/local.conf")
        appendline = "require conf/fragments/{}.inc\n".format(args.fragmentname)

        with open(confpath) as f:
            lines = f.readlines()
            for l in lines:
                if l == appendline:
                    print("Fragment {} already included in {}".format(args.fragmentname, confpath))
                    return

        lines.append(appendline)
        with open(confpath, 'w') as f:
            f.write(''.join(lines))

    def do_remove_fragment(self, args):
        """ Remove a fragment from the local build configuration """
        confpath = os.path.join(os.environ["BBPATH"], "conf/local.conf")
        appendline = "require conf/fragments/{}.inc\n".format(args.fragmentname)

        with open(confpath) as f:
            lines = f.readlines()
        lines = [l for l in lines if l != appendline]

        with open(confpath, 'w') as f:
            f.write(''.join(lines))

    def register_commands(self, sp):
        parser_list_fragments = self.add_command(sp, 'list-fragments', self.do_list_fragments, parserecipes=False)
        parser_list_fragments.add_argument('--verbose', '-v', action='store_true', help='Print extended descriptions of the fragments')

        parser_add_fragment = self.add_command(sp, 'add-fragment', self.do_add_fragment, parserecipes=False)
        parser_add_fragment.add_argument('fragmentname', help='The name of the fragment (use list-fragments to see them)')

        parser_remove_fragment = self.add_command(sp, 'remove-fragment', self.do_remove_fragment, parserecipes=False)
        parser_remove_fragment.add_argument('fragmentname', help='The name of the fragment')
