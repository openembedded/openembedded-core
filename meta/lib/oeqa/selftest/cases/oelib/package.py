#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import os
import shutil
import tempfile
import unittest.mock
from unittest.case import TestCase

import oe.path
from oe.package import copydebugsources, splitdebuginfo


class FakeDataStore:
    def __init__(self, values):
        self.values = values

    def getVar(self, name):
        return self.values.get(name)

    def expand(self, value):
        for name, replacement in self.values.items():
            value = value.replace("${%s}" % name, replacement)
        return value


class TestCopyDebugSources(TestCase):
    def setUp(self):
        for tool in ("cpio", "find", "mv"):
            if shutil.which(tool) is None:
                self.skipTest("Required tool %s not found" % tool)

    def test_copydebugsources_copies_files_and_dereferences_links(self):
        with tempfile.TemporaryDirectory(prefix="oe-test-package-") as tmpdir:
            source_root = os.path.join(tmpdir, "source")
            second_source_root = os.path.join(tmpdir, "second-source")
            workdir = os.path.join(tmpdir, "work")
            pkgd = os.path.join(tmpdir, "pkgd")
            debugsrcdir = "/usr/src/debug/testpkg/1.0"
            second_debugsrcdir = "/usr/src/debug/secondpkg/1.0"

            os.makedirs(os.path.join(source_root, "nested"))
            os.makedirs(second_source_root)
            os.makedirs(workdir)
            os.makedirs(pkgd)

            normal = os.path.join(source_root, "nested", "normal.c")
            special = os.path.join(source_root, "nested", "name with ; spaces.c")
            leading_dash = os.path.join(source_root, "nested", "-leading-dash.c")
            recipe_sysroot_basename = os.path.join(source_root, "nested",
                                                   "recipe-sysroot-file.c")
            internal = os.path.join(source_root, "nested", "compiler<internal>")
            built_in = os.path.join(source_root, "nested", "compiler<built-in>")
            target = os.path.join(source_root, "nested", "target.c")
            link = os.path.join(source_root, "nested", "link.c")
            ignored_src = os.path.join(source_root, "recipe-sysroot", "ignored.c")
            ignored_native_src = os.path.join(source_root,
                                             "foo-recipe-sysroot-native",
                                             "ignored.c")
            second = os.path.join(second_source_root, "second.c")

            with open(normal, "w") as f:
                f.write("normal\n")
            with open(special, "w") as f:
                f.write("special\n")
            with open(leading_dash, "w") as f:
                f.write("leading dash\n")
            with open(recipe_sysroot_basename, "w") as f:
                f.write("recipe sysroot basename\n")
            with open(internal, "w") as f:
                f.write("internal\n")
            with open(built_in, "w") as f:
                f.write("built in\n")
            with open(target, "w") as f:
                f.write("target\n")
            os.symlink("target.c", link)
            os.makedirs(os.path.dirname(ignored_src))
            with open(ignored_src, "w") as f:
                f.write("ignored\n")
            os.makedirs(os.path.dirname(ignored_native_src))
            with open(ignored_native_src, "w") as f:
                f.write("ignored native\n")
            with open(second, "w") as f:
                f.write("second\n")

            empty_dir = oe.path.join(pkgd, debugsrcdir, "empty", "dir")
            os.makedirs(empty_dir)

            sources = [
                os.path.join(debugsrcdir, "nested", "normal.c"),
                os.path.join(debugsrcdir, "nested", "name with ; spaces.c"),
                os.path.join(debugsrcdir, "nested", "-leading-dash.c"),
                os.path.join(debugsrcdir, "nested", "recipe-sysroot-file.c"),
                os.path.join(debugsrcdir, "nested", "compiler<internal>"),
                os.path.join(debugsrcdir, "nested", "compiler<built-in>"),
                os.path.join(debugsrcdir, "nested", "link.c"),
                os.path.join(debugsrcdir, "recipe-sysroot", "ignored.c"),
                os.path.join(debugsrcdir, "foo-recipe-sysroot-native",
                             "ignored.c"),
                os.path.join(second_debugsrcdir, "second.c"),
            ]
            d = FakeDataStore({
                "WORKDIR": workdir,
                "PKGD": pkgd,
                "STRIP": "strip",
                "OBJCOPY": "objcopy",
                "S": os.path.join(workdir, "source"),
                "CFLAGS": (
                    "-ffile-prefix-map=%s=%s "
                    "-ffile-prefix-map=%s=%s"
                ) % (
                    source_root,
                    debugsrcdir,
                    second_source_root,
                    second_debugsrcdir,
                ),
            })

            copydebugsources(debugsrcdir, sources, d)

            copied_normal = oe.path.join(pkgd, debugsrcdir,
                                         "nested", "normal.c")
            copied_special = oe.path.join(pkgd, debugsrcdir,
                                          "nested", "name with ; spaces.c")
            copied_leading_dash = oe.path.join(pkgd, debugsrcdir,
                                               "nested", "-leading-dash.c")
            copied_recipe_sysroot_basename = oe.path.join(
                pkgd, debugsrcdir, "nested", "recipe-sysroot-file.c")
            copied_internal = oe.path.join(pkgd, debugsrcdir,
                                           "nested", "compiler<internal>")
            copied_built_in = oe.path.join(pkgd, debugsrcdir,
                                           "nested", "compiler<built-in>")
            copied_link = oe.path.join(pkgd, debugsrcdir, "nested", "link.c")
            copied_second = oe.path.join(pkgd, second_debugsrcdir, "second.c")
            ignored = oe.path.join(pkgd, debugsrcdir,
                                   "recipe-sysroot", "ignored.c")
            ignored_native = oe.path.join(pkgd, debugsrcdir,
                                          "foo-recipe-sysroot-native",
                                          "ignored.c")

            with open(copied_normal) as f:
                self.assertEqual(f.read(), "normal\n")
            with open(copied_special) as f:
                self.assertEqual(f.read(), "special\n")
            with open(copied_leading_dash) as f:
                self.assertEqual(f.read(), "leading dash\n")
            with open(copied_recipe_sysroot_basename) as f:
                self.assertEqual(f.read(), "recipe sysroot basename\n")
            with open(copied_link) as f:
                self.assertEqual(f.read(), "target\n")
            with open(copied_second) as f:
                self.assertEqual(f.read(), "second\n")
            self.assertFalse(os.path.islink(copied_link))
            self.assertFalse(os.path.exists(copied_internal))
            self.assertFalse(os.path.exists(copied_built_in))
            self.assertFalse(os.path.exists(ignored))
            self.assertFalse(os.path.exists(ignored_native))
            self.assertFalse(os.path.exists(empty_dir))

    def test_copydebugsources_ignores_copy_failure(self):
        with tempfile.TemporaryDirectory(prefix="oe-test-package-") as tmpdir:
            source_root = os.path.join(tmpdir, "source")
            workdir = os.path.join(tmpdir, "work")
            pkgd = os.path.join(tmpdir, "pkgd")
            debugsrcdir = "/usr/src/debug/testpkg/1.0"
            mapped_debugsrcdir = os.path.join(debugsrcdir, "mapped")

            os.makedirs(source_root)
            os.makedirs(workdir)
            os.makedirs(pkgd)

            sources = [
                os.path.join(mapped_debugsrcdir, "missing.c"),
            ]
            d = FakeDataStore({
                "WORKDIR": workdir,
                "PKGD": pkgd,
                "STRIP": "strip",
                "OBJCOPY": "objcopy",
                "S": os.path.join(workdir, "source"),
                "CFLAGS": "-ffile-prefix-map=%s=%s" % (
                    source_root,
                    mapped_debugsrcdir,
                ),
            })

            copydebugsources(debugsrcdir, sources, d)

            self.assertFalse(os.path.exists(oe.path.join(pkgd, mapped_debugsrcdir)))
            self.assertFalse(os.path.exists(oe.path.join(pkgd, debugsrcdir)))

    def test_copydebugsources_moves_externalsrc_relocation(self):
        with tempfile.TemporaryDirectory(prefix="oe-test-package-") as tmpdir:
            source_root = os.path.join(tmpdir, "external-[source]")
            workdir = os.path.join(tmpdir, "work")
            pkgd = os.path.join(tmpdir, "pkgd")
            debugsrcdir = "/usr/src/debug/testpkg/1.0"

            os.makedirs(source_root)
            os.makedirs(workdir)
            os.makedirs(pkgd)

            source = os.path.join(source_root, "real.c")
            with open(source, "w") as f:
                f.write("real\n")

            relocation = oe.path.join(pkgd, debugsrcdir, source_root)
            relocated_name = "-relocated with ; spaces.c"
            os.makedirs(relocation)
            with open(os.path.join(relocation, relocated_name), "w") as f:
                f.write("relocated\n")
            with open(oe.path.join(pkgd, debugsrcdir, relocated_name), "w") as f:
                f.write("old\n")

            sources = [os.path.join(debugsrcdir, "real.c")]
            d = FakeDataStore({
                "WORKDIR": workdir,
                "PKGD": pkgd,
                "STRIP": "strip",
                "OBJCOPY": "objcopy",
                "S": source_root,
                "CFLAGS": "-ffile-prefix-map=%s=%s" % (source_root, debugsrcdir),
            })

            copydebugsources(debugsrcdir, sources, d)

            copied_source = oe.path.join(pkgd, debugsrcdir, "real.c")
            moved_source = oe.path.join(pkgd, debugsrcdir, relocated_name)

            with open(copied_source) as f:
                self.assertEqual(f.read(), "real\n")
            with open(moved_source) as f:
                self.assertEqual(f.read(), "relocated\n")
            self.assertFalse(os.path.exists(relocation))

    def test_copydebugsources_ignores_empty_externalsrc_relocation(self):
        with tempfile.TemporaryDirectory(prefix="oe-test-package-") as tmpdir:
            source_root = os.path.join(tmpdir, "external-source")
            workdir = os.path.join(tmpdir, "work")
            pkgd = os.path.join(tmpdir, "pkgd")
            debugsrcdir = "/usr/src/debug/testpkg/1.0"

            os.makedirs(source_root)
            os.makedirs(workdir)
            os.makedirs(pkgd)

            source = os.path.join(source_root, "real.c")
            with open(source, "w") as f:
                f.write("real\n")

            relocation = oe.path.join(pkgd, debugsrcdir, source_root)
            os.makedirs(relocation)

            sources = [os.path.join(debugsrcdir, "real.c")]
            d = FakeDataStore({
                "WORKDIR": workdir,
                "PKGD": pkgd,
                "STRIP": "strip",
                "OBJCOPY": "objcopy",
                "S": source_root,
                "CFLAGS": "-ffile-prefix-map=%s=%s" % (source_root, debugsrcdir),
            })

            copydebugsources(debugsrcdir, sources, d)

            copied_source = oe.path.join(pkgd, debugsrcdir, "real.c")

            with open(copied_source) as f:
                self.assertEqual(f.read(), "real\n")
            self.assertFalse(os.path.exists(relocation))


class TestSplitDebugInfoSignedModule(TestCase):
    # The kernel appends this exact 28 byte marker to a signed module, which is
    # what oe.package.is_kernel_module_signed() looks for.
    SIGNATURE = b"~Module signature appended~\n"

    def _make_module(self, pkgd, signed):
        module = os.path.join(pkgd, "usr", "lib", "modules", "1.0",
                              "kernel", "net", "testmod", "testmod.ko")
        os.makedirs(os.path.dirname(module))
        with open(module, "wb") as f:
            f.write(b"\x7fELF" + b"\x00" * 64)
            if signed:
                f.write(self.SIGNATURE)
        return module

    def _dv(self):
        return {
            "libdir": "/usr/lib/debug",
            "dir": "/.debug",
            "append": "",
            "srcdir": "/usr/src/debug",
        }

    def test_signed_module_keeps_debug_sources(self):
        with tempfile.TemporaryDirectory(prefix="oe-test-package-") as tmpdir:
            pkgd = os.path.join(tmpdir, "pkgd")
            os.makedirs(pkgd)
            module = self._make_module(pkgd, signed=True)
            dv = self._dv()

            self.assertTrue(oe.package.is_kernel_module_signed(module),
                            "test module was not recognised as signed")

            with open(module, "rb") as f:
                before = f.read()

            d = FakeDataStore({"PKGD": pkgd, "OBJCOPY": "objcopy"})
            expected = ["/usr/src/debug/testmod/1.0/testmod.c"]
            with unittest.mock.patch("oe.package.source_info",
                                     return_value=expected) as source_info:
                _, sources = splitdebuginfo(module, pkgd, dv, d)

            # The debug sources must still be collected, even though the
            # module itself is left alone.
            source_info.assert_called_once_with(module, d)
            self.assertEqual(sources, expected)

            # The module must be untouched so that its signature stays valid.
            with open(module, "rb") as f:
                self.assertEqual(f.read(), before)

    def test_signed_module_without_srcdir_collects_nothing(self):
        with tempfile.TemporaryDirectory(prefix="oe-test-package-") as tmpdir:
            pkgd = os.path.join(tmpdir, "pkgd")
            os.makedirs(pkgd)
            module = self._make_module(pkgd, signed=True)
            dv = self._dv()
            dv["srcdir"] = ""

            d = FakeDataStore({"PKGD": pkgd, "OBJCOPY": "objcopy"})
            with unittest.mock.patch("oe.package.source_info") as source_info:
                _, sources = splitdebuginfo(module, pkgd, dv, d)

            source_info.assert_not_called()
            self.assertEqual(sources, [])
