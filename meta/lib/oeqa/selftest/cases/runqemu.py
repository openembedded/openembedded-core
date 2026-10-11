#
# Copyright (c) 2017 Wind River Systems, Inc.
#
# SPDX-License-Identifier: MIT
#

import os
import re
import time
import fcntl
import glob
import json
import runpy
import shutil
import socket
import stat
import subprocess
import sys
import tempfile
from concurrent.futures import ThreadPoolExecutor
from types import SimpleNamespace
from unittest.mock import patch
import oe.types
from oeqa.core.decorator import OETestTag
from oeqa.core.decorator.data import skipIfNotArch, skipIfNotMachine
from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, runqemu, get_bb_var, get_bb_vars
from oeqa.utils.qemurunner import QemuRunner


@OETestTag("runqemu")
class RunqemuSlirpTests(OESelftestTestCase):
    """Exercise forwarding and legacy arbitration without booting a guest."""

    @classmethod
    def setUpClass(cls):
        """Locate the native QEMU and load the QMP client and runqemu classes.

        Builds qemu-helper-native's sysroot once for the whole class, picks its
        qemu-system binary, and imports the QMP client shipped with it. runqemu
        is loaded as a module so its BaseConfig and RunQemuError can be used
        directly.
        """
        super().setUpClass()
        bitbake('qemu-helper-native -c addto_recipe_sysroot')
        bindir = get_bb_var('STAGING_BINDIR_NATIVE', 'qemu-helper-native')
        cls.qemu_binary = sorted(glob.glob(os.path.join(bindir, 'qemu-system-*')))[0]
        sys.path.append(os.path.join(os.path.dirname(os.path.dirname(bindir)), 'usr/lib/qemu-python'))
        from qmp.legacy import QEMUMonitorProtocol
        cls.monitor_protocol = QEMUMonitorProtocol
        runqemu_module = runpy.run_path(shutil.which('runqemu'))
        cls.base_config = runqemu_module['BaseConfig']
        cls.runqemu_error = runqemu_module['RunQemuError']

    def setUpLocal(self):
        """Start a bare QEMU with a slirp netdev and connect runqemu and the runner to it.

        QEMU runs paused with no machine, so no guest boots. It exposes two QMP
        sockets: an internal one used by runqemu's BaseConfig and a separate
        monitor one used by the QemuRunner. Both see the same netdev, so
        forwards added through either are visible to the other. Everything is
        cleaned up after each test. Only the port-lock directory is mocked;
        flock and QEMU's lock descriptor transfer remain real.
        """
        directory = tempfile.TemporaryDirectory(prefix='slirp-selftest-')
        self.addCleanup(directory.cleanup)
        self.port_lock_dir = os.path.join(directory.name, 'port-locks')
        self.config = self.base_config()
        config_lockdir = patch.object(self.config, 'PORT_LOCK_DIR', self.port_lock_dir)
        config_lockdir.start()
        self.addCleanup(config_lockdir.stop)
        self.config.internal_qmp_path = os.path.join(directory.name, 'internal.sock')
        self.config.slirp_hostfwd_allowlist = []
        self.addCleanup(self.config.release_lock)
        self.runner = QemuRunner('', '', '', directory.name, directory.name,
                                 None, 30, directory.name, False, self.logger,
                                 use_slirp=True, workdir=directory.name)
        runner_lockdir = patch.object(self.runner, 'PORT_LOCK_DIR', self.port_lock_dir)
        runner_lockdir.start()
        self.addCleanup(runner_lockdir.stop)
        self.addCleanup(self.runner.stop)
        monitor_path = os.path.join(directory.name, 'monitor.sock')
        self.process = subprocess.Popen([
            self.qemu_binary, '-machine', 'none', '-nodefaults', '-display', 'none',
            '-S', '-netdev', 'user,id=net0',
            '-qmp', 'unix:%s,server=on,wait=off' % self.config.internal_qmp_path,
            '-qmp', 'unix:%s,server=on,wait=off' % monitor_path,
        ], stdout=subprocess.DEVNULL, stderr=subprocess.PIPE)
        self.addCleanup(self._stop_qemu)
        self.config.add_slirp_hostfwds()
        self.runner.qmp = self.monitor_protocol(monitor_path)
        self.runner.qmp.settimeout(5)
        self.assertIn('QMP', self.runner.qmp.connect())

    def _stop_qemu(self):
        if self.process.poll() is None:
            self.process.terminate()
        self.process.communicate(timeout=10)

    def _monitor(self, command, arguments=None, timeout=60):
        return self.runner.qmp.cmd_raw(command, arguments)

    def _socket(self, protocol='tcp'):
        probe = socket.socket(socket.AF_INET, socket.SOCK_STREAM if protocol == 'tcp' else socket.SOCK_DGRAM)
        self.addCleanup(probe.close)
        probe.bind(('127.0.0.1', 0))
        if protocol == 'tcp':
            probe.listen()
        return probe

    def _occupied_port(self, protocol='tcp'):
        """Return a port held by this test, so QEMU must reject it as a proposal."""
        return self._socket(protocol).getsockname()[1]

    # TODO: legacy-slirp-locks
    def _lock(self, port):
        lock = open(os.path.join(self.port_lock_dir, '%d.lock' % port), 'w')
        self.addCleanup(lock.close)
        fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        return lock

    def _assert_locked(self, port):
        with open(os.path.join(self.port_lock_dir, '%d.lock' % port), 'w') as lock:
            with self.assertRaises(BlockingIOError):
                fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)

    def _startup_forward(self, port, protocol='tcp', guest_port=1234, host='127.0.0.1'):
        self.config.slirp_hostfwd_allowlist = [(protocol, host, port, '', guest_port)]
        self.config.add_slirp_hostfwds()
        return self.runner.get_host_port(guest_port, protocol=protocol)

    def test_startup_legacy_reservation(self):
        """Skip a startup port locked by an older runqemu and lock the replacement.

        Older releases (wrynose, blacksail) reserve ports via flock on
        /tmp/qemu-port-locks/<port>.lock. runqemu must not take a port whose
        lock is held, must lock the port it picks instead, and the forward on
        that port must accept connections.

        The port is also occupied, so QEMU would reject it; the lock refusal
        is checked on runqemu's own lock attempt instead.
        """
        port = self._occupied_port()
        self._lock(port)
        lockfile = os.path.join(self.port_lock_dir, '%d.lock' % port)
        results = {}
        acquire_lock = self.config.acquire_lock

        def spy(path):
            results.setdefault(path, acquire_lock(path))
            return results[path]
        with patch.object(self.config, 'acquire_lock', side_effect=spy):
            actual = self._startup_forward(port)
        self.assertIs(results[lockfile], False)
        self.assertNotEqual(actual, port)
        self._assert_locked(actual)
        with socket.create_connection(('127.0.0.1', actual), timeout=5):
            pass

    def test_startup_rejected_candidate(self):
        """Release a rejected startup port's lock and lock the replacement.

        The requested port is bound by another process, so its lock is free
        but QEMU rejects hostfwd_add. runqemu must release the lock it took
        for the rejected port (it must be lockable again afterwards), then
        retry on the next port and keep that port's lock held.
        """
        busy = self._socket()
        port = busy.getsockname()[1]
        actual = self._startup_forward(port)
        self.assertNotEqual(actual, port)
        self._lock(port)
        self._assert_locked(actual)

    def test_dynamic_legacy_reservation(self):
        """Reject explicit legacy-reserved ports and skip them during allocation."""
        port = self._occupied_port()
        self._lock(port)
        self.assertIsNone(self.runner.add_hostfwd(1234, port))
        with patch('oeqa.utils.qemurunner.socket.socket') as probe, \
             patch.object(self.runner, 'run_monitor') as monitor:
            probe.return_value.getsockname.return_value = ('127.0.0.1', port)
            with self.assertRaises(RuntimeError):
                self.runner.add_hostfwd(1234, protocol='udp')
            for call in monitor.call_args_list:
                self.assertNotIn('hostfwd_add', call.args[1]['command-line'])

    def test_dynamic_rejected_candidates(self):
        """Release dynamic port locks when binding or monitor commands fail."""
        for protocol in ('tcp', 'udp'):
            with self.subTest(protocol=protocol):
                busy = self._socket(protocol)
                port = busy.getsockname()[1]
                self.assertIsNone(self.runner.add_hostfwd(1234, port, protocol=protocol))
                self._lock(port)
        port = self._occupied_port()
        for failure in (None, {}, {'error': {}}, {'return': '', 'error': {}},
                        {'return': 'Could not set up host forwarding rule'}, {'return': 0}):
            with self.subTest(response=failure):
                def reply(command, arguments=None):
                    if arguments and arguments['command-line'].startswith('hostfwd_add'):
                        self._assert_locked(port)
                        return failure
                    return self._monitor(command, arguments)
                with patch.object(self.runner, 'run_monitor', side_effect=reply):
                    self.assertIsNone(self.runner.add_hostfwd(1234, port))
                self._lock(port).close()
        with patch.object(self.runner, 'run_monitor', side_effect=[{'return': ''}, RuntimeError('monitor disconnected')]):
            with self.assertRaises(RuntimeError):
                self.runner.add_hostfwd(1234, port)
        self._lock(port)

    def test_lock_cleanup(self):
        """Release startup and dynamic port locks after QEMU and its clients stop."""
        proposed = self._occupied_port()
        startup_port = self._startup_forward(proposed)
        self.assertNotEqual(startup_port, proposed)
        dynamic_port = self.runner.add_hostfwd(4321, protocol='udp')
        self._assert_locked(startup_port)
        self._assert_locked(dynamic_port)
        self._stop_qemu()
        self.runner.stop()
        self.config.release_lock()
        self._lock(startup_port)
        self._lock(dynamic_port)

    def test_locks_survive_client_exit(self):
        """Keep port locks in QEMU after runqemu and the runner close their copies.

        Closing the clients' lock descriptors and monitor connection must not
        release either forward's lock or UDP socket while QEMU is alive.
        Both locks must become available once QEMU exits.
        """
        proposed = self._occupied_port('udp')
        startup_port = self._startup_forward(proposed, protocol='udp')
        self.assertNotEqual(startup_port, proposed)
        dynamic_port = self.runner.add_hostfwd(4321, protocol='udp')
        # TODO: legacy-slirp-locks: simulate client death.
        self.config.release_lock()
        for lock in self.runner._slirp_port_locks.values():
            lock.close()
        self.runner._slirp_port_locks.clear()
        self.runner.qmp.close()
        self.runner.qmp = None
        self.assertIsNone(self.process.poll())
        for port in (startup_port, dynamic_port):
            self._assert_locked(port)
            with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as probe:
                with self.assertRaises(OSError):
                    probe.bind(('127.0.0.1', port))
        self._stop_qemu()
        self._lock(startup_port)
        self._lock(dynamic_port)

    def test_internal_monitor_timeouts(self):
        """Report stalled QMP exchanges and release the rejected forward's lock."""
        self.config.SLIRP_QMP_TIMEOUT = 1
        for phase in ('greeting', 'capabilities', 'hostfwd'):
            with self.subTest(phase=phase), tempfile.TemporaryDirectory() as directory:
                port = self._occupied_port()
                self.config.internal_qmp_path = os.path.join(directory, 'silent.sock')
                self.config.slirp_hostfwd_allowlist = [('tcp', '127.0.0.1', port, '', 1234)]
                with socket.socket(socket.AF_UNIX, socket.SOCK_STREAM) as listener:
                    listener.settimeout(5)
                    listener.bind(self.config.internal_qmp_path)
                    listener.listen()
                    with ThreadPoolExecutor(max_workers=1) as executor:
                        future = executor.submit(self.config.add_slirp_hostfwds)
                        with listener.accept()[0] as peer:
                            peer.settimeout(5)
                            with peer.makefile('rw') as stream:
                                if phase != 'greeting':
                                    stream.write(json.dumps({'QMP': {}}) + '\n')
                                    stream.flush()
                                    self.assertEqual(json.loads(stream.readline())['execute'], 'qmp_capabilities')
                                if phase == 'hostfwd':
                                    stream.write(json.dumps({'return': {}}) + '\n')
                                    stream.flush()
                                    self.assertEqual(json.loads(stream.readline())['execute'], 'human-monitor-command')
                                with self.assertRaisesRegex(self.runqemu_error, 'Timed out communicating'):
                                    future.result(timeout=5)
                if phase == 'hostfwd':
                    self._lock(port)

    def test_private_internal_monitor(self):
        """Add a private forwarding monitor without replacing the requested monitor.

        The internal socket's directory must be accessible only to its owner
        and registered for cleanup. Both QMP sockets must appear in QEMU's
        command line.
        """
        self.config.slirp_hostfwd_allowlist = [('tcp', '127.0.0.1', 2222, '', 22)]
        self.config.qmp = 'unix:requested-monitor.sock'
        self.config.setup_qmp()
        directory = os.path.dirname(self.config.internal_qmp_path)
        self.addCleanup(shutil.rmtree, directory)
        self.assertEqual(stat.S_IMODE(os.stat(directory).st_mode), 0o700)
        self.assertIn(directory, self.config.cleanup_files)
        self.assertIn('-qmp unix:requested-monitor.sock,server,nowait', self.config.qemu_opt)
        self.assertIn('-qmp unix:%s,server,nowait' % self.config.internal_qmp_path, self.config.qemu_opt)

    def test_mac_slot_reuse(self):
        """Allocate distinct MAC slots and reuse a slot after its lock is released.

        A third configuration must take the first configuration's freed slot
        while the second configuration continues to hold its own slot.
        """
        with tempfile.TemporaryDirectory() as directory:
            configs = [self.base_config() for index in range(3)]
            try:
                self.assertEqual(configs[0].alloc_mac_slot(directory), 1)
                self.assertEqual(configs[1].alloc_mac_slot(directory), 2)
                configs[0].release_lock()
                self.assertEqual(configs[2].alloc_mac_slot(directory), 1)
            finally:
                for config in configs:
                    config.release_lock()

    def test_mac_slot_exhaustion(self):
        """Report exhaustion only after trying all 254 usable MAC slot locks."""
        with tempfile.TemporaryDirectory() as directory:
            with patch.object(self.config, 'acquire_lock', return_value=False) as acquire:
                with self.assertRaisesRegex(self.runqemu_error, 'Too many concurrent'):
                    self.config.alloc_mac_slot(directory)
            self.assertEqual(acquire.call_count, 254)
            self.assertEqual([os.path.basename(call.args[0]) for call in acquire.call_args_list],
                             ['%d.lock' % slot for slot in range(1, 255)])

    def test_bind_addresses(self):
        """Preserve explicit bind addresses and resolve wildcard binds to localhost."""
        for guest_port, host, expected in ((1234, '127.0.0.2', '127.0.0.2'), (1235, '0.0.0.0', '127.0.0.1')):
            with self.subTest(host=host):
                actual = self._startup_forward(self._occupied_port(), guest_port=guest_port, host=host)
                endpoint = self.runner._get_slirp_host_endpoint(guest_port)
                self.assertEqual(endpoint, (expected, actual))
                with socket.create_connection(endpoint, timeout=5):
                    pass

    def test_unsupported_protocol(self):
        """Reject unsupported forwarding protocols without acquiring port locks."""
        with self.assertRaises(ValueError):
            self.runner.add_hostfwd(1234, protocol='sctp')
        self.assertFalse(self.runner._slirp_port_locks)

    def test_explicit_forward_with_non_slirp_primary(self):
        """Manage slirp forwards independently of the primary networking mode."""
        self.runner.use_slirp = False
        for protocol in ('tcp', 'udp'):
            with self.subTest(protocol=protocol):
                port = self.runner.add_hostfwd(1234, protocol=protocol)
                self.assertEqual(self.runner.slirp_port_mappings[(protocol, 1234)], port)
                self.assertEqual(self.runner.get_host_port(1234, protocol=protocol), port)
        port = self.runner.get_host_port(1236)
        self.assertEqual(self.runner.slirp_port_mappings[('tcp', 1236)], port)
        with socket.create_connection(('127.0.0.1', port), timeout=5):
            pass
        response = self.runner.run_monitor('netdev_del', {'id': 'net0'})
        self.assertEqual(response, {'return': {}})
        port = self._occupied_port()
        self.assertIsNone(self.runner.add_hostfwd(1235, port))
        self._lock(port)
        with self.assertRaisesRegex(RuntimeError, 'Failed to add a slirp tcp hostfwd'):
            self.runner.get_host_port(1235)


@OETestTag("runqemu")
class RunqemuTests(OESelftestTestCase):
    """Runqemu test class"""

    image_is_ready = False
    deploy_dir_image = ''

    def setUpLocal(self):
        super(RunqemuTests, self).setUpLocal()
        self.recipe = 'core-image-minimal'
        self.machine = self.td['MACHINE']
        self.image_link_name = get_bb_var('IMAGE_LINK_NAME', self.recipe)

        self.fstypes = "ext4"
        if self.td["HOST_ARCH"] in ('i586', 'i686', 'x86_64'):
            self.fstypes += " iso hddimg"
        if self.machine == "qemux86-64":
            self.fstypes += " wic.vmdk wic.qcow2 wic.vdi wic.zst"

        self.cmd_common = "runqemu nographic snapshot"
        kvm = oe.types.qemu_use_kvm(get_bb_var('QEMU_USE_KVM'), self.td["TARGET_ARCH"])
        if kvm:
            self.cmd_common += " kvm"

        self.write_config(
"""
IMAGE_FSTYPES = "%s"
# 10 means 1 second
SYSLINUX_TIMEOUT = "10"
""" % self.fstypes)

        if not RunqemuTests.image_is_ready:
            RunqemuTests.deploy_dir_image = get_bb_var('DEPLOY_DIR_IMAGE')
            bitbake(self.recipe)
            RunqemuTests.image_is_ready = True

    def test_boot_machine(self):
        """Test runqemu machine"""
        cmd = "%s %s" % (self.cmd_common, self.machine)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

    def test_boot_machine_ext4(self):
        """Test runqemu machine ext4"""
        cmd = "%s %s ext4" % (self.cmd_common, self.machine)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                regexp = r'\nROOTFS: .*\.ext4]\n'
                self.assertRegex(f.read(), regexp, "Failed to find '%s' in '%s' after running '%s'" % (regexp, qemu.qemurunnerlog, cmd))

    @skipIfNotArch(['i586', 'i686', 'x86_64'])
    def test_boot_machine_iso(self):
        """Test runqemu machine iso"""
        cmd = "%s %s iso" % (self.cmd_common, self.machine)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                text_in = 'media=cdrom'
                self.assertIn(text_in, f.read(), "Failed to find '%s' in '%s' after running '%s'" % (text_in, qemu.qemurunnerlog, cmd))

    def test_boot_recipe_image(self):
        """Test runqemu recipe-image"""
        cmd = "%s %s" % (self.cmd_common, self.recipe)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

    # https://bugzilla.yoctoproject.org/show_bug.cgi?id=14963
    @skipIfNotMachine("qemux86-64", "tests are qemux86-64 specific currently")
    def test_boot_recipe_image_vmdk(self):
        """Test runqemu recipe-image vmdk"""
        cmd = "%s %s wic.vmdk" % (self.cmd_common, self.recipe)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                text_in = 'format=vmdk'
                self.assertIn(text_in, f.read(), "Failed to find '%s' in '%s' after running '%s'" % (text_in, qemu.qemurunnerlog, cmd))

    @skipIfNotMachine("qemux86-64", "tests are qemux86-64 specific currently")
    def test_boot_recipe_image_vdi(self):
        """Test runqemu recipe-image vdi"""
        cmd = "%s %s wic.vdi" % (self.cmd_common, self.recipe)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                text_in = 'format=vdi'
                self.assertIn(text_in, f.read(), "Failed to find '%s' in '%s' after running '%s'" % (text_in, qemu.qemurunnerlog, cmd))

    def test_boot_deploy(self):
        """Test runqemu deploy_dir_image"""
        cmd = "%s %s" % (self.cmd_common, self.deploy_dir_image)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

    @skipIfNotArch(['i586', 'i686', 'x86_64'])
    def test_boot_deploy_hddimg(self):
        """Test runqemu deploy_dir_image hddimg"""
        cmd = "%s %s hddimg" % (self.cmd_common, self.deploy_dir_image)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(re.search('file=.*.hddimg', f.read()), "Failed: %s, %s" % (cmd, f.read()))

    def test_boot_machine_slirp(self):
        """Test runqemu machine slirp"""
        cmd = "%s slirp %s" % (self.cmd_common, self.machine)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertIn(' -netdev user', f.read(), "Failed: %s" % cmd)

    @skipIfNotMachine("qemux86-64", "tests are qemux86-64 specific currently")
    def test_boot_machine_slirp_qcow2(self):
        """Test runqemu machine slirp qcow2"""
        cmd = "%s slirp wic.qcow2 %s" % (self.cmd_common, self.machine)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertIn('format=qcow2', f.read(), "Failed: %s" % cmd)

    def test_boot_qemu_boot(self):
        """Test runqemu /path/to/image.qemuboot.conf"""
        qemuboot_conf = "%s.qemuboot.conf" % (self.image_link_name)
        qemuboot_conf = os.path.join(self.deploy_dir_image, qemuboot_conf)
        if not os.path.exists(qemuboot_conf):
            self.skipTest("%s not found" % qemuboot_conf)
        cmd = "%s %s" % (self.cmd_common, qemuboot_conf)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

    def test_boot_rootfs(self):
        """Test runqemu /path/to/rootfs.ext4"""
        rootfs = "%s.ext4" % (self.image_link_name)
        rootfs = os.path.join(self.deploy_dir_image, rootfs)
        if not os.path.exists(rootfs):
            self.skipTest("%s not found" % rootfs)
        cmd = "%s %s" % (self.cmd_common, rootfs)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

    @skipIfNotMachine("qemux86-64", "wic tests are qemux86-64 specific currently")
    def test_boot_compressed_wic_by_path(self):
        """Test runqemu /path/to/rootfs.wic.zst"""
        rootfs = "%s.wic.zst" % (self.image_link_name)
        rootfs = os.path.join(self.deploy_dir_image, rootfs)
        if not os.path.exists(rootfs):
            self.skipTest("%s not found" % rootfs)
        cmd = "%s %s snapshot" % (self.cmd_common, rootfs)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

    @skipIfNotMachine("qemux86-64", "wic tests are qemux86-64 specific currently")
    def test_boot_compressed_wic_by_recipe(self):
        """Test runqemu recipe-image wic.zst"""
        cmd = "%s %s snapshot wic.zst" % (self.cmd_common, self.recipe)
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            with open(qemu.qemurunnerlog) as f:
                self.assertTrue(qemu.runner.logged, "Failed: %s, %s" % (cmd, f.read()))

# This test was designed as a separate class to test that shutdown
# command will shutdown qemu as expected on each qemu architecture
# based on the MACHINE configuration inside the config file
# (eg. local.conf).
#
# This was different compared to RunqemuTests, where RunqemuTests was
# dedicated for MACHINE=qemux86-64 where it test that qemux86-64 will
# bootup various filesystem types, including live image(iso and hddimg)
# where live image was not supported on all qemu architecture.
@OETestTag("machine")
@OETestTag("runqemu")
class QemuTest(OESelftestTestCase):

    @classmethod
    def setUpClass(cls):
        super(QemuTest, cls).setUpClass()
        cls.recipe = 'core-image-minimal'
        cls.image_fstypes = "tar.zst"

        cls.write_config(cls,
"""
IMAGE_FSTYPES += "%s"
""" % cls.image_fstypes)

        bb_vars = get_bb_vars([
            'DEPLOY_DIR_IMAGE',
            'IMAGE_LINK_NAME',
            'MACHINE',
            ],
            cls.recipe)
        cls.deploy_dir_image = bb_vars['DEPLOY_DIR_IMAGE']
        cls.image_link_name = bb_vars['IMAGE_LINK_NAME']
        cls.machine = bb_vars['MACHINE']
        cls.cmd_common = "runqemu nographic snapshot"
        cls.qemuboot_conf = "%s.qemuboot.conf" % (cls.image_link_name)
        cls.qemuboot_conf = os.path.join(cls.deploy_dir_image, cls.qemuboot_conf)

        bitbake(cls.recipe)

    def _start_qemu_shutdown_check_if_shutdown_succeeded(self, qemu, timeout):
        # Allow the runner's LoggingThread instance to exit without errors
        # (such as the exception "Console connection closed unexpectedly")
        # as qemu will disappear when we shut it down
        qemu.runner.allowexit()
        qemu.run_serial("shutdown -h now")
        time_track = 0
        try:
            while True:
                is_alive = qemu.check()
                if not is_alive:
                    return True
                if time_track > timeout:
                    return False
                time.sleep(1)
                time_track += 1
        except SystemExit:
            return True

    def test_qemu_can_shutdown(self):
        self.assertExists(self.qemuboot_conf)
        cmd = "%s snapshot %s" % (self.cmd_common, self.qemuboot_conf)
        shutdown_timeout = 120
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            qemu_shutdown_succeeded = self._start_qemu_shutdown_check_if_shutdown_succeeded(qemu, shutdown_timeout)
            self.assertTrue(qemu_shutdown_succeeded, 'Failed: %s does not shutdown within timeout(%s)' % (self.machine, shutdown_timeout))

    def test_qemu_can_boot_nfs_and_shutdown(self):
        rootfs_tar = "%s.%s" % (self.image_link_name, self.image_fstypes)
        rootfs_tar = os.path.join(self.deploy_dir_image, rootfs_tar)
        self.assertExists(rootfs_tar)
        cmd = "%s %s" % (self.cmd_common, rootfs_tar)
        shutdown_timeout = 120
        with runqemu(self.recipe, ssh=False, launch_cmd=cmd) as qemu:
            qemu_shutdown_succeeded = self._start_qemu_shutdown_check_if_shutdown_succeeded(qemu, shutdown_timeout)
            self.assertTrue(qemu_shutdown_succeeded, 'Failed: %s does not shutdown within timeout(%s)' % (self.machine, shutdown_timeout))
