import os, subprocess, unittest
import bb
from oeqa.sdk.case import OESDKTestCase

from oeqa.utils.subprocesstweak import errors_have_output
errors_have_output()

class BuildAssimp(OESDKTestCase):
    """
    Test case to build a project using cmake.
    """

    td_vars = ['DATETIME', 'TARGET_OS', 'TARGET_ARCH']

    def setUp(self):
        if not (self.tc.hasHostPackage("nativesdk-cmake") or
                self.tc.hasHostPackage("cmake-native")):
            raise unittest.SkipTest("Needs cmake")

    def test_assimp(self):
        import tempfile
        with tempfile.TemporaryDirectory(prefix="assimp", dir=self.tc.sdk_dir) as testdir:
            tarball = self.fetch(testdir, self.td["DL_DIR"], "https://github.com/assimp/assimp/archive/v4.1.0.tar.gz")
            subprocess.check_output(["tar", "xf", tarball, "-C", testdir])

            sourcedir = os.path.join(testdir, "assimp-4.1.0") 
            builddir = os.path.join(testdir, "build")
            installdir = os.path.join(testdir, "install")
            bb.utils.mkdirhier(builddir)

            self._run("cd %s && cmake -DCMAKE_VERBOSE_MAKEFILE:BOOL=ON %s " % (builddir, sourcedir))
            self._run("cmake --build %s -- -j" % builddir)
            self._run("cmake --build %s --target install -- DESTDIR=%s" % (builddir, installdir))
            self.check_elf(os.path.join(installdir, "usr", "local", "lib", "libassimp.so.4.1.0"))
