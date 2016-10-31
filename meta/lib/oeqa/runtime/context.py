# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os

from oeqa.core.context import OETestContext, OETestContextExecutor
from oeqa.core.target.ssh import OESSHTarget
from oeqa.runtime.loader import OERuntimeTestLoader

class OERuntimeTestContext(OETestContext):
    loaderClass = OERuntimeTestLoader

    def __init__(self, td, logger, target, packages_manifest):
        super(OERuntimeTestContext, self).__init__(td, logger)
        self.target = target
        self.image_packages = self.readPackagesManifest(packages_manifest)
        self._set_target_cmds()

    def _set_target_cmds(self):
        self.target_cmds = {}

        self.target_cmds['ps'] = 'ps'
        if 'procps' in self.image_packages:
            self.target_cmds['ps'] = self.target_cmds['ps'] + ' -ef'

    def readPackagesManifest(self, manifest):
        if not os.path.exists(manifest):
            raise OSError("Couldn't find manifest file: %s" % manifest)

        image_packages = set()
        with open(manifest, 'r') as f:
            for line in f.readlines():
                line = line.strip()
                if line and not line.startswith("#"):
                    image_packages.add(line.split()[0])

        return image_packages

class OERuntimeTestContextExecutor(OETestContextExecutor):
    _context_class = OERuntimeTestContext

    name = 'runtime'
    help = 'runtime test component'
    description = 'executes runtime tests over targets'
    default_cases = os.path.join(os.path.abspath(os.path.dirname(__file__)),
            'cases')
    default_target_ip = '192.168.7.2'

    def register_commands(self, logger, subparsers):
        super(OERuntimeTestContextExecutor, self).register_commands(logger, subparsers)
        self.parser.add_argument('--target-ip', action='store',
                default=self.default_target_ip,
                help="IP address of device under test, default: %s" \
                % self.default_target_ip)
        self.parser.add_argument('--packages-manifest', action='store',
                help="Package manifest of the image under test")

    def _process_args(self, logger, args):
        if not args.packages_manifest:
            raise TypeError('Manifest file not provided')

        super(OERuntimeTestContextExecutor, self)._process_args(logger, args)
        target = OESSHTarget(args.target_ip)
        self.tc_kwargs['init']['target'] = target

        packages_manifest = os.path.join(os.getcwd(), args.packages_manifest)
        self.tc_kwargs['init']['packages_manifest'] = packages_manifest

_executor_class = OERuntimeTestContextExecutor
