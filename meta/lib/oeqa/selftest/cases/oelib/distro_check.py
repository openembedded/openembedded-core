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

META_RELEASE = b"""Dist: noble
Name: Noble Numbat
Version: 24.04.5 LTS
Supported: 1

Dist: questing
Name: Questing Quokka
Version: 25.10
Supported: 0

Dist: resolute
Name: Resolute Raccoon
Version: 26.04.1 LTS
Supported: 1

Dist: zzz
Name: Not supported any more
Version: 99.10
Supported: 0
"""

SOURCES_WITH_BINARIES = b"""Package: brotli
Binary: python3-brotli, brotli, libbrotli-dev,
 libbrotli1
Version: 1.1.0-2
Description: an example
 Binary: not-a-package

Package: six
Binary: python3-six
"""

class TestDebianStyleBinaryPackages(TestCase):
    def test_binary_packages(self):
        with mock.patch.object(oe.distro_check, "create_socket", return_value=io.BytesIO(gzip.compress(SOURCES_WITH_BINARIES))):
            names = oe.distro_check.get_debian_style_source_package_list("http://example.com/Sources.gz", "main", None)
        self.assertEqual(names, {"brotli:main", "python3-brotli:main", "libbrotli-dev:main", "libbrotli1:main",
                                 "six:main", "python3-six:main"})

class TestFindLatestUbuntuRelease(TestCase):
    def test_latest_supported(self):
        with mock.patch.object(oe.distro_check, "create_socket", return_value=io.BytesIO(META_RELEASE)):
            self.assertEqual(oe.distro_check.find_latest_ubuntu_release(None), "resolute")

class TestUbuntuPackageList(TestCase):
    def test_main_and_universe(self):
        """
        The Ubuntu list has the packages in universe as well as main, from
        the release and from its updates.
        """
        sources = {
            "resolute/main": b"Package: bash\n",
            "resolute/universe": b"Package: nasm\n",
            "resolute-updates/main": b"Package: openssl\n",
            "resolute-updates/universe": b"Package: nginx\n",
        }

        def fake_socket(url, d):
            dist, component = url.split("/dists/")[1].split("/")[:2]
            return io.BytesIO(gzip.compress(sources[dist + "/" + component]))

        with mock.patch.object(oe.distro_check, "find_latest_ubuntu_release", return_value="resolute"), \
                mock.patch.object(oe.distro_check, "create_socket", side_effect=fake_socket):
            latest, names = oe.distro_check.get_latest_released_ubuntu_source_package_list(None)

        self.assertEqual(latest, "resolute")
        self.assertEqual(names, {"bash:main", "nasm:universe", "openssl:updates", "nginx:updates"})

class TestOpenSUSE(TestCase):
    LISTING = b"""<a href="../">..</a> <a href="./15.0-Current/">15.0-Current/</a>
<a href="./15.6/">15.6/</a> <a href="./16.0/">16.0/</a> <a href="./42.3/">42.3/</a>"""

    def test_links_relative_to_current_directory(self):
        with mock.patch.object(oe.distro_check, "create_socket", return_value=io.BytesIO(self.LISTING)):
            links = oe.distro_check.get_links_from_url("http://example.com/leap/", None)
        self.assertEqual(links, ["..", "15.0-Current", "15.6", "16.0", "42.3"])

    def test_latest_leap_release(self):
        # Leap 42.x came before 15.0
        links = ["..", "15.0-Current", "15.6", "16.0", "16.1", "42.2", "42.3", "42.3-Current"]
        with mock.patch.object(oe.distro_check, "get_links_from_url", return_value=links):
            self.assertEqual(oe.distro_check.find_latest_opensuse_leap_release("http://example.com/leap/", None), "16.1")

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
                    for name in ("ubuntu", "fedora", "opensuse")}
        fetchers["get_latest_released_debian_source_package_list"] = fail

        with tempfile.TemporaryDirectory() as tmpdir, \
                mock.patch.multiple(oe.distro_check, **fetchers), \
                mock.patch.object(bb, "note"), \
                mock.patch.object(bb, "warn") as warn:
            oe.distro_check.create_distro_packages_list(tmpdir, None)
            lists = sorted(os.listdir(os.path.join(tmpdir, "package_lists")))

        self.assertEqual(lists, ["Fedora-1.0", "Ubuntu-1.0", "openSUSE-1.0"])
        warn.assert_called_once()
        self.assertIn("Debian", warn.call_args[0][0])
