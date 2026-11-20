#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import base64
from unittest.case import TestCase

import oe.npm

CHECKSUM = bytes(range(64)).hex()
INTEGRITY = "sha512-" + base64.b64encode(bytes(range(64))).decode()


def shrinkwrap(packages):
    """Return a shrinkwrap file with the given dependencies"""
    return {"lockfileVersion": 3, "packages": {"": {"name": "main"}, **packages}}


def registry(package, version, host="registry.npmjs.org"):
    """Return a dependency from a registry"""
    basename = package.split("/")[-1]
    return {
        "version": version,
        "resolved": "https://%s/%s/-/%s-%s.tgz" % (host, package, basename, version),
        "integrity": INTEGRITY,
    }


class TestShrinkwrapSrcUri(TestCase):
    def test_registry(self):
        entries = oe.npm.shrinkwrap_src_uri(shrinkwrap({
            "node_modules/send": registry("send", "0.19.0"),
            "node_modules/send/node_modules/ms": registry("ms", "2.1.3"),
            "node_modules/@scope/pkg": registry("@scope/pkg", "1.0.0", "npm.example.com/path"),
        }), "npm")
        self.assertEqual(entries, [
            "npm://npm.example.com/path/;package=@scope/pkg;version=1.0.0;sha512sum=%s;destsuffix=npm/node_modules/@scope/pkg" % CHECKSUM,
            "npm://registry.npmjs.org/;package=send;version=0.19.0;sha512sum=%s;destsuffix=npm/node_modules/send" % CHECKSUM,
            "npm://registry.npmjs.org/;package=ms;version=2.1.3;sha512sum=%s;destsuffix=npm/node_modules/send/node_modules/ms" % CHECKSUM,
        ])

    def test_alias(self):
        # An alias installs a package under another name: the "name" field
        # gives the real package
        dep = registry("real", "1.0.0")
        dep["name"] = "real"
        entries = oe.npm.shrinkwrap_src_uri(shrinkwrap({"node_modules/alias": dep}), "npm")
        self.assertEqual(entries, [
            "npm://registry.npmjs.org/;package=real;version=1.0.0;sha512sum=%s;destsuffix=npm/node_modules/alias" % CHECKSUM,
        ])

    def test_tarball(self):
        entries = oe.npm.shrinkwrap_src_uri(shrinkwrap({
            "node_modules/foo": {
                "version": "1.0.0",
                "resolved": "https://example.com/downloads/foo.tgz",
                "integrity": INTEGRITY,
            },
        }), "npm")
        self.assertEqual(entries, [
            "https://example.com/downloads/foo.tgz;downloadfilename=npm2/foo.tgz;sha512sum=%s;subdir=npm/node_modules/foo;striplevel=1" % CHECKSUM,
        ])

    def test_git(self):
        entries = oe.npm.shrinkwrap_src_uri(shrinkwrap({
            "node_modules/foo": {
                "version": "1.0.0",
                "resolved": "git+ssh://git@github.com/example/foo.git#0123456789abcdef0123456789abcdef01234567",
            },
        }), "npm")
        self.assertEqual(entries, [
            "git://git@github.com/example/foo.git;protocol=ssh;rev=0123456789abcdef0123456789abcdef01234567;nobranch=1;destsuffix=npm/node_modules/foo",
        ])

    def test_skipped(self):
        dev = registry("dev", "1.0.0")
        dev["dev"] = True
        bundled = registry("bundled", "1.0.0")
        bundled["inBundle"] = True
        no_resolved = registry("dup", "1.0.0")
        del no_resolved["resolved"]
        packages = {
            "node_modules/dev": dev,
            "node_modules/bundled": bundled,
            "node_modules/dup": no_resolved,
        }
        self.assertEqual(oe.npm.shrinkwrap_src_uri(shrinkwrap(packages), "npm"), [])
        self.assertEqual(len(oe.npm.shrinkwrap_src_uri(shrinkwrap(packages), "npm", dev=True)), 1)

    def test_unsupported(self):
        for dep in ({"version": "1.0.0", "resolved": "file:../foo"},
                    {"link": True, "resolved": "../foo"},
                    {"version": "1.0.0"}):
            with self.assertRaises(ValueError):
                oe.npm.shrinkwrap_src_uri(shrinkwrap({"node_modules/foo": dep}), "npm")


class TestSrcUriDifferences(TestCase):
    EXPECTED = [
        "npm://registry.npmjs.org/;package=foo;version=1.0.0;sha512sum=%s;destsuffix=npm/node_modules/foo" % CHECKSUM,
        "npm://registry.npmjs.org/;package=bar;version=2.0.0;sha512sum=%s;destsuffix=npm/node_modules/bar" % CHECKSUM,
    ]

    def test_match(self):
        # The order of the entries and of the URL parameters does not matter.
        # Entries that do not unpack into node_modules are not extra.
        src_uri = [
            "npm://registry.npmjs.org/;package=main;version=1.0.0;sha512sum=%s" % CHECKSUM,
            "file://npm-shrinkwrap.json;subdir=npm",
            self.EXPECTED[1],
            "npm://registry.npmjs.org/;destsuffix=npm/node_modules/foo;sha512sum=%s;version=1.0.0;package=foo" % CHECKSUM,
        ]
        self.assertEqual(oe.npm.src_uri_differences(self.EXPECTED, src_uri), ([], []))

    def test_missing_and_extra(self):
        old_bar = "npm://registry.npmjs.org/;package=bar;version=1.0.0;sha512sum=%s;destsuffix=npm/node_modules/bar" % CHECKSUM
        missing, extra = oe.npm.src_uri_differences(self.EXPECTED, [self.EXPECTED[0], old_bar])
        self.assertEqual(missing, [self.EXPECTED[1]])
        self.assertEqual(extra, [old_bar])
