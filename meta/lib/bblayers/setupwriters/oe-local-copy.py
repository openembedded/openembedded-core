#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import logging
import json

logger = logging.getLogger('bitbake-layers')

def plugin_init(plugins):
    return OeLocalCopyWriter()

class OeLocalCopyWriter():

    def __str__(self):
        return "oe-local-copy"

    def do_write(self, parent, args):
        """ Writes out a local copy of all the metadata layers (and bitbake) included in a current build. """
        if not os.path.exists(args.destdir):
            os.makedirs(args.destdir)
        repos = parent.make_repo_config(args.destdir)
        if not repos:
            raise Exception("Could not determine layer sources")
        output = os.path.join(os.path.abspath(args.destdir), args.output_prefix or "layers")
        json_f = os.path.join(os.path.abspath(args.destdir), "bundle-repos.json")

        for r in repos.values():
            r['git-remote']['remotes'] = {"origin":{"uri":r["originpath"]}}

        json_data = {"version":"1.0","sources":repos}
        with open(json_f, 'w') as f:
            json.dump(json_data, f, sort_keys=True, indent=4)

        logger.info("Cloning repositories into {}".format(output))
        bb.process.run('oe-setup-layers --force-bootstraplayer-checkout --destdir {} --jsondata {}'.format(output, json_f))

    def register_arguments(self, parser):
        pass
