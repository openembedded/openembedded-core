#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#
import os
from subprocess import Popen, PIPE
import threading
import time

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, get_bb_var, get_bb_vars, runqemu , runCmd
from oeqa.core.exception import OEQATimeoutError


#The test runs gdbserver on qemu and connects the gdb client from host over TCP.
#
#It builds a cross gdb on the host and compiles the program to be debugged on the target,
#launches the gdbserver and tries to connect cross gdb to it.


class GdbTest(OESelftestTestCase):

    def run_gdb(self, native_sysroot=None, **options):
        # Add path to cross gdb to environment.
        extra_paths = "%s/usr/bin/%s" % (self.recipe_sysroot_native, self.target_sys)
        nenv = dict(options.get('env', os.environ))
        nenv['PATH'] = extra_paths + ':' + nenv.get('PATH', '')
        options['env'] = nenv

        for count in range(5):
            # Let the gdb server start before by putting client thread to sleep.
            # Still, if gdb client happens to start before gdbserver, if will
            # return "gdb connection timed out". In that case try
            # connecting again
            time.sleep(1)
            cmd = "%s-gdb -ex 'set sysroot %s' -ex \"target remote %s:%s\" -ex continue -ex quit %s" \
                       %(self.target_sys, self.recipe_sysroot_native, self.qemu_ip, self.gdbserver_port, self.binary)
            r = runCmd(cmd, native_sysroot=self.recipe_sysroot_native, **options)
            if "Connection timed out" not in r.output:
                break
            if count == 4:
                self.assertTrue(False, "gdb unable to connect to gdbserver")

    def run_gdb_client(self):
        self.run_gdb(native_sysroot=self.recipe_sysroot_native, ignore_status=False)

    def test_gdb_server(self):
        self.target_dst = "/tmp/"
        self.source  = "test.c"
        self.binary  = "test"
        self.target_source = self.target_dst + self.source
        self.target_binary = self.target_dst + self.binary
        self.gdbserver_port = 2001

        try:
            # These aren't the actual IP addresses but testexport class needs something defined
            features  = 'TEST_SERVER_IP = "192.168.7.1"\n'
            features += 'TEST_TARGET_IP = "192.168.7.2"\n'
            features += 'EXTRA_IMAGE_FEATURES += "ssh-server-openssh"\n'
            features += 'CORE_IMAGE_EXTRA_INSTALL += "gdbserver packagegroup-core-buildessential"\n'
            self.write_config(features)

            self.target_arch = get_bb_var('TARGET_ARCH')
            self.target_sys  = get_bb_var('TARGET_SYS')

            recipe = "gdb-cross-%s" %self.target_arch
            gdb_cross_bitbake_command = "%s -c addto_recipe_sysroot" %recipe

            bitbake(gdb_cross_bitbake_command)

            self.recipe_sysroot_native = get_bb_var('RECIPE_SYSROOT_NATIVE', recipe)

            bitbake('core-image-minimal')

            self.cross_gdb_name = "%s-gdb" %self.target_sys

            # wrap the execution with a qemu instance
            with runqemu("core-image-minimal", runqemuparams = "nographic") as qemu:
                status, target_gcc = qemu.run_serial("which gcc")

                self.assertNotEqual(target_gcc, None, 'gcc not found on the target')

                self.qemu_ip = qemu.ip

                # self.tc.files_dir = meta/lib/oeqa/files
                src = os.path.join(self.tc.files_dir, 'test.c')

                self.logger.debug("src : %s qemu.ip : %s self.target_dst : %s" % (src , self.qemu_ip , self.target_dst))
                cmd = "scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR %s root@%s:%s"  % (src, qemu.ip, self.target_dst)
                result = runCmd(cmd)

                cmd = "cat %s" %(self.target_source)
                status, output = qemu.run_serial(cmd)
                self.assertIn("1234", output, "Source file copied to target is different that what is expected")

                cmd = "%s %s -o %s -lm -g" % (target_gcc, self.target_source, self.target_binary)
                status, output = qemu.run_serial(cmd)

                cmd = "ls %s" %self.target_binary
                status, output = qemu.run_serial(cmd)
                self.assertNotIn("No such file or directory", output, "Test file compilation failed on target")

                status, output = qemu.run_serial(self.target_binary)
                self.assertIn("1234", output, "Test binary is different than what is expected")

                cmd = "scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR root@%s:%s %s"  % (qemu.ip, self.target_binary, os.getcwd())
                result = runCmd(cmd)

                cmd = "strip -s --strip-debug %s" %self.target_binary
                status, output = qemu.run_serial(cmd)

                gdb_client_thread = threading.Thread(target=self.run_gdb_client)
                gdb_client_thread.start()

                cmd = "gdbserver localhost:%s %s" %(self.gdbserver_port, self.target_binary)
                status, output = qemu.run_serial(cmd)
                self.logger.debug("gdbserver status : %s output : %s" % (status, output))

                gdb_client_thread.join()
                self.assertIn("1234", output, "Expected string (1234) not present in test output")

        except OEQATimeoutError:
            self.fail("gdbserver test timeout error")

