# SPDX-License-Identifier: MIT
import os
import contextlib
from oeqa.core.decorator import OETestTag
from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, get_bb_var, get_bb_vars, runqemu, Command
from oeqa.utils.nfs import unfs_server

def parse_values(content):
    for i in content:
        for v in ["PASS", "FAIL", "XPASS", "XFAIL", "UNRESOLVED", "UNSUPPORTED", "UNTESTED", "ERROR", "WARNING"]:
            if i.startswith(v + ": "):
                yield i[len(v) + 2:].strip(), v
                break

@OETestTag("machine")
class GlibcSelfTest(OESelftestTestCase):
    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        if not hasattr(cls.tc, "extraresults"):
            cls.tc.extraresults = {}

        if "ptestresult.sections" not in cls.tc.extraresults:
            cls.tc.extraresults["ptestresult.sections"] = {}

    def test_glibc(self):
        self.glibc_run_check()

    def glibc_run_check(self, ssh = None):
        # configure ssh target
        features = []
        if ssh is not None:
            features.append('TOOLCHAIN_TEST_TARGET = "ssh"')
            features.append('TOOLCHAIN_TEST_HOST = "{0}"'.format(ssh))
            features.append('TOOLCHAIN_TEST_HOST_USER = "root"')
            features.append('TOOLCHAIN_TEST_HOST_PORT = "22"')
            # force single threaded test execution
            features.append('EGLIBCPARALLELISM_task-check_pn-glibc-testsuite = "PARALLELMFLAGS="-j1""')
        self.write_config("\n".join(features))

        bitbake("glibc-testsuite -c check")

        builddir = get_bb_var("B", "glibc-testsuite")

        failed = 0
        self.tc.extraresults["ptestresult.sections"]["glibc"] = {}
        with open(os.path.join(builddir, "tests.sum"), "r") as f:
            for test, result in parse_values(f):
                self.tc.extraresults["ptestresult.glibc.{}".format(test)] = {"status" : result}
                if result == "FAIL":
                    self.logger.info("failed: '{}'".format(test))
                    failed += 1
        self.assertEqual(failed, 0)

class GlibcSelfTestSystemEmulated(GlibcSelfTest):
    default_installed_packages = [
        "glibc-charmaps",
        "libgcc",
        "libstdc++",
        "libatomic",
        "libgomp",
        "python3",
        "python3-pexpect",
        "nfs-utils",
        ]

    def glibc_run_check(self):
        with contextlib.ExitStack() as s:
            # use the base work dir, as the nfs mount, since the recipe directory may not exist
            tmpdir = get_bb_var("BASE_WORKDIR")
            nfsport, mountport = s.enter_context(unfs_server(tmpdir))

            # build core-image-minimal with required packages
            features = []
            features.append('IMAGE_FEATURES += "ssh-server-openssh"')
            features.append('CORE_IMAGE_EXTRA_INSTALL += "{0}"'.format(" ".join(self.default_installed_packages)))
            self.write_config("\n".join(features))
            bitbake("core-image-minimal")

            # start runqemu
            qemu = s.enter_context(runqemu("core-image-minimal", runqemuparams = "nographic"))

            # validate that SSH is working
            status, _ = qemu.run("uname")
            self.assertEqual(status, 0)

            # setup nfs mount
            if qemu.run("mkdir -p \"{0}\"".format(tmpdir))[0] != 0:
                raise Exception("Failed to setup NFS mount directory on target")
            mountcmd = "mount -o noac,nfsvers=3,port={0},udp,mountport={1} \"{2}:{3}\" \"{3}\"".format(nfsport, mountport, qemu.server_ip, tmpdir)
            status, output = qemu.run(mountcmd)
            if status != 0:
                raise Exception("Failed to setup NFS mount on target ({})".format(repr(output)))

            super().glibc_run_check(ssh = qemu.ip)

