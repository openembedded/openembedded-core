#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

"""Explicit SRC_URI entries for the dependencies of an npm shrinkwrap file."""

import os
import re

import bb.fetch
import bb.utils
from bb.fetch.npm import npm_integrity, npm_localfile
from bb.fetch.npmsw import foreach_dependencies

GIT_RESOLVED_RE = re.compile(r"^git\+(?P<protocol>[a-z]+)://(?P<url>[^#]+)#(?P<rev>[0-9a-f]+)$")


def shrinkwrap_src_uri(shrinkwrap, destdir, dev=False):
    """Return one SRC_URI entry for each dependency of a shrinkwrap file.

    shrinkwrap is the content of npm-shrinkwrap.json (lockfileVersion 2 or
    later). destdir is the directory of the main package, relative to
    UNPACKDIR. Each dependency unpacks to its location in destdir (for example
    destdir/node_modules/foo), where npm.bbclass expects it. dev also adds the
    devDependencies. The entries are sorted by location.

    A package from a registry gets an npm:// entry, so that the fetcher knows
    its name and version. Other tarballs get a plain URL and git dependencies
    a git:// URL. Each entry has the checksum or revision of the shrinkwrap
    file.
    """
    entries = {}

    def _add(name, data, location):
        package = data.get("name", name)
        version = data.get("version")
        resolved = data.get("resolved")
        integrity = data.get("integrity")
        destsuffix = os.path.join(destdir, location)

        if not resolved:
            # npm leaves out "resolved" for some duplicate packages. npm.bbclass
            # does not need these, as npmsw already skipped them.
            if version and integrity:
                return
            raise ValueError("%s: no 'resolved' field" % location)

        if data.get("link") or resolved.startswith("file:"):
            raise ValueError("%s: local dependencies are not supported" % location)

        match = GIT_RESOLVED_RE.match(resolved)
        if match:
            entries[location] = "git://%s;protocol=%s;rev=%s;nobranch=1;destsuffix=%s" % (
                match["url"], match["protocol"], match["rev"], destsuffix)
            return

        if not integrity or not resolved.startswith(("https://", "http://")):
            raise ValueError("%s: unsupported dependency %s" % (location, resolved))

        checksum_name, checksum = npm_integrity(integrity)
        basename = package.split("/")[-1]
        suffix = "/%s/-/%s-%s.tgz" % (package, basename, version)
        if resolved.startswith("https://") and resolved.endswith(suffix) and bb.utils.is_semver(version):
            registry = resolved[len("https://"):-len(suffix)]
            entries[location] = "npm://%s/;package=%s;version=%s;%s=%s;destsuffix=%s" % (
                registry, package, version, checksum_name, checksum, destsuffix)
        else:
            entries[location] = "%s;downloadfilename=%s;%s=%s;subdir=%s;striplevel=1" % (
                resolved, npm_localfile(os.path.basename(resolved)), checksum_name, checksum, destsuffix)

    foreach_dependencies(shrinkwrap, _add, dev)
    return [entries[location] for location in sorted(entries)]


def src_uri_differences(expected, src_uri):
    """Compare the expected entries with SRC_URI.

    Returns (missing, extra): the expected entries that SRC_URI does not have,
    and the SRC_URI entries that unpack into a node_modules directory but are
    not expected. Entries are compared after parsing, so the order of the URL
    parameters does not matter.
    """
    def _key(url):
        scheme, host, path, user, password, parm = bb.fetch.decodeurl(url)
        return (scheme, host, path, user, password, tuple(sorted(parm.items())))

    def _unpacks_to_node_modules(url):
        parm = bb.fetch.decodeurl(url)[5]
        dest = parm.get("destsuffix") or parm.get("subdir") or ""
        return "node_modules/" in dest + "/"

    expected_keys = {_key(url): url for url in expected}
    actual_keys = {_key(url): url for url in src_uri}
    missing = [url for key, url in expected_keys.items() if key not in actual_keys]
    extra = [url for key, url in actual_keys.items()
             if key not in expected_keys and _unpacks_to_node_modules(url)]
    return missing, extra
