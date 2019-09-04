# SPDX-License-Identifier: MIT
import os
from oeqa.core.decorator import OETestTag
from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, get_bb_var, get_bb_vars, runqemu, Command

def parse_values(content):
    for i in content:
        for v in ["PASS", "FAIL", "XPASS", "XFAIL", "UNRESOLVED", "UNSUPPORTED", "UNTESTED", "ERROR", "WARNING"]:
            if i.startswith(v + ": "):
                yield i[len(v) + 2:].strip(), v
                break

@OETestTag("machine")
class GccSelfTest(OESelftestTestCase):
    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        if not hasattr(cls.tc, "extraresults"):
            cls.tc.extraresults = {}

        if "ptestresult.sections" not in cls.tc.extraresults:
            cls.tc.extraresults["ptestresult.sections"] = {}

    def gcc_runtime_check_skip(self, suite):
        targets = get_bb_var("RUNTIMETARGET", "gcc-runtime").split()
        if suite not in targets:
            self.skipTest("Target does not use {0}".format(suite))

    def test_cross_gcc(self):
        self.gcc_run_check("gcc", "g++")

    def test_libatomic(self):
        self.gcc_run_check("libatomic")

    def test_libgomp(self):
        self.gcc_run_check("libgomp")

    def test_libstdcxx(self):
        self.gcc_run_check("libstdc++-v3")

    def test_libssp(self):
        self.gcc_runtime_check_skip("libssp")
        self.gcc_run_check("libssp")

    def test_libitm(self):
        self.gcc_runtime_check_skip("libitm")
        self.gcc_run_check("libitm")

    def gcc_run_check(self, *suites, ssh = None):
        targets = set()
        for s in suites:
            if s in ["gcc", "g++"]:
                targets.add("check-gcc")
            else:
                targets.add("check-target-{}".format(s))

        # configure ssh target
        features = []
        features.append('MAKE_CHECK_TARGETS = "{0}"'.format(" ".join(targets)))
        if ssh is not None:
            features.append('TOOLCHAIN_TEST_TARGET = "ssh"')
            features.append('TOOLCHAIN_TEST_HOST = "{0}"'.format(ssh))
            features.append('TOOLCHAIN_TEST_HOST_USER = "root"')
            features.append('TOOLCHAIN_TEST_HOST_PORT = "22"')
        self.write_config("\n".join(features))

        recipe = "gcc-runtime"
        bitbake("{} -c check".format(recipe))

        bb_vars = get_bb_vars(["B", "TARGET_SYS"], recipe)
        builddir, target_sys = bb_vars["B"], bb_vars["TARGET_SYS"]

        for suite in suites:
            sumspath = os.path.join(builddir, "gcc", "testsuite", suite, "{0}.sum".format(suite))
            if not os.path.exists(sumspath): # check in target dirs
                sumspath = os.path.join(builddir, target_sys, suite, "testsuite", "{0}.sum".format(suite))
            if not os.path.exists(sumspath): # handle libstdc++-v3 -> libstdc++
                sumspath = os.path.join(builddir, target_sys, suite, "testsuite", "{0}.sum".format(suite.split("-")[0]))

            ptestsuite = "gcc-{}".format(suite) if suite != "gcc" else suite
            ptestsuite = ptestsuite + "-user" if ssh is None else ptestsuite
            self.tc.extraresults["ptestresult.sections"][ptestsuite] = {}
            with open(sumspath, "r") as f:
                for test, result in parse_values(f):
                    self.tc.extraresults["ptestresult.{}.{}".format(ptestsuite, test)] = {"status" : result}

class GccSelfTestSystemEmulated(GccSelfTest):
    default_installed_packages = ["libgcc", "libstdc++", "libatomic", "libgomp"]

    def gcc_run_check(self, *args, **kwargs):
        # build core-image-minimal with required packages
        features = []
        features.append('IMAGE_FEATURES += "ssh-server-openssh"')
        features.append('CORE_IMAGE_EXTRA_INSTALL += "{0}"'.format(" ".join(self.default_installed_packages)))
        self.write_config("\n".join(features))
        bitbake("core-image-minimal")

        # wrap the execution with a qemu instance
        with runqemu("core-image-minimal", runqemuparams = "nographic") as qemu:
            # validate that SSH is working
            status, _ = qemu.run("uname")
            self.assertEqual(status, 0)

            return super().gcc_run_check(*args, ssh=qemu.ip, **kwargs)

