# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os

from oeqa.core.context import OETestContext, OETestContextExecutor
from oeqa.core.target.ssh import OESSHTarget
from oeqa.core.target.qemu import OEQemuTarget
from oeqa.utils.dump import HostDumper

from oeqa.runtime.loader import OERuntimeTestLoader

class OERuntimeTestContext(OETestContext):
    loaderClass = OERuntimeTestLoader
    runtime_files_dir = os.path.join(
                        os.path.dirname(os.path.abspath(__file__)), "files")

    def __init__(self, td, logger, target,
                 host_dumper, image_packages, extract_dir):
        super(OERuntimeTestContext, self).__init__(td, logger)

        self.target = target
        self.image_packages = image_packages
        self.host_dumper = host_dumper
        self.extract_dir = extract_dir
        self._set_target_cmds()

    def _set_target_cmds(self):
        self.target_cmds = {}

        self.target_cmds['ps'] = 'ps'
        if 'procps' in self.image_packages:
            self.target_cmds['ps'] = self.target_cmds['ps'] + ' -ef'

class OERuntimeTestContextExecutor(OETestContextExecutor):
    _context_class = OERuntimeTestContext

    name = 'runtime'
    help = 'runtime test component'
    description = 'executes runtime tests over targets'

    default_cases = os.path.join(os.path.abspath(os.path.dirname(__file__)),
            'cases')
    default_data = None

    default_target_type = 'simpleremote'
    default_server_ip = '192.168.7.1'
    default_target_ip = '192.168.7.2'
    default_host_dumper_dir = '/tmp/oe-saved-tests'
    default_extract_dir = 'extract_dir'

    def register_commands(self, logger, subparsers):
        super(OERuntimeTestContextExecutor, self).register_commands(logger, subparsers)

        runtime_group = self.parser.add_argument_group('runtime options')

        runtime_group.add_argument('--target-type', action='store',
                default=self.default_target_type, choices=['simpleremote', 'qemu'],
                help="Target type of device under test, default: %s" \
                % self.default_target_type)
        runtime_group.add_argument('--target-ip', action='store',
                default=self.default_target_ip,
                help="IP address of device under test, default: %s" \
                % self.default_target_ip)
        runtime_group.add_argument('--server-ip', action='store',
                default=self.default_target_ip,
                help="IP address of device under test, default: %s" \
                % self.default_server_ip)

        runtime_group.add_argument('--host-dumper-dir', action='store',
                default=self.default_host_dumper_dir,
                help="Directory where host status is dumped, if tests fails, default: %s" \
                % self.default_host_dumper_dir)

        runtime_group.add_argument('--packages-manifest', action='store',
                help="Package manifest of the image under test")

        runtime_group.add_argument('--extract-dir', action='store',
                help='Directory where extracted packages reside')

        runtime_group.add_argument('--qemu-boot', action='store',
                help="Qemu boot configuration, only needed when target_type is QEMU.")

    @staticmethod
    def getTarget(target_type, logger, target_ip, server_ip, **kwargs):
        target = None

        if target_type == 'simpleremote':
            target = OESSHTarget(logger, target_ip, server_ip, **kwargs)
        elif target_type == 'qemu':
            target = OEQemuTarget(logger, target_ip, server_ip, **kwargs)
        else:
            # TODO: Implement custom target module loading
            raise TypeError("target_type %s isn't supported" % target_type)

        return target

    @staticmethod
    def readPackagesManifest(manifest):
        if not os.path.exists(manifest):
            raise OSError("Manifest file not exists: %s" % manifest)

        image_packages = set()
        with open(manifest, 'r') as f:
            for line in f.readlines():
                line = line.strip()
                if line and not line.startswith("#"):
                    image_packages.add(line.split()[0])

        return image_packages

    @staticmethod
    def getHostDumper(cmds, directory):
        return HostDumper(cmds, directory)

    def _process_args(self, logger, args):
        if not args.packages_manifest:
            raise TypeError('Manifest file not provided')

        super(OERuntimeTestContextExecutor, self)._process_args(logger, args)

        target_kwargs = {}
        target_kwargs['qemuboot'] = args.qemu_boot

        self.tc_kwargs['init']['target'] = \
                OERuntimeTestContextExecutor.getTarget(args.target_type,
                        args.target_ip, args.server_ip, **target_kwargs)
        self.tc_kwargs['init']['host_dumper'] = \
                OERuntimeTestContextExecutor.getHostDumper(None,
                        args.host_dumper_dir)
        self.tc_kwargs['init']['image_packages'] = \
                OERuntimeTestContextExecutor.readPackagesManifest(
                        args.packages_manifest)

        self.tc_kwargs['init']['extract_dir'] = \
                OERuntimeTestContextExecutor.readPackagesManifest(
                        args.extract_dir)

_executor_class = OERuntimeTestContextExecutor
