#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import gzip
import io
import lzma
import os
import tempfile
from unittest import mock
from unittest.case import TestCase

import bb
# oe.distro_check uses bb as a builtin, which bb.build sets up
import bb.build
import oe.distro_check

SOURCES = b"Package: foo\nVersion: 1.0\n\nPackage: bar\nVersion: 2.0\n"

class TestDebianStyleSourcePackageList(TestCase):
    def check(self, url, data):
        with mock.patch.object(oe.distro_check, "create_socket", return_value=io.BytesIO(data)):
            names = oe.distro_check.get_debian_style_source_package_list(url, "main", None)
        self.assertEqual(names, {"foo:main", "bar:main"})

    def test_gzip(self):
        self.check("http://example.com/Sources.gz", gzip.compress(SOURCES))

    def test_xz(self):
        self.check("http://example.com/Sources.xz", lzma.compress(SOURCES))

class TestCreateDistroPackagesList(TestCase):
    def test_skip_failed_distro(self):
        """
        A distro whose package list can't be fetched is skipped, and the
        other distros' lists are still written.
        """
        def fail(d):
            raise OSError("HTTP Error 404: Not Found")

        def ok(d):
            return "1.0", {"foo:main"}

        fetchers = {"get_latest_released_%s_source_package_list" % name: ok
                    for name in ("ubuntu", "fedora", "opensuse", "clear")}
        fetchers["get_latest_released_debian_source_package_list"] = fail

        with tempfile.TemporaryDirectory() as tmpdir, \
                mock.patch.multiple(oe.distro_check, **fetchers), \
                mock.patch.object(bb, "note"), \
                mock.patch.object(bb, "warn") as warn:
            oe.distro_check.create_distro_packages_list(tmpdir, None)
            lists = sorted(os.listdir(os.path.join(tmpdir, "package_lists")))

        self.assertEqual(lists, ["Clear-1.0", "Fedora-1.0", "Ubuntu-1.0", "openSUSE-1.0"])
        warn.assert_called_once()
        self.assertIn("Debian", warn.call_args[0][0])
