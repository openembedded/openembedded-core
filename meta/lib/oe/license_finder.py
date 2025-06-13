#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import fnmatch
import hashlib
import logging
import os
import re

import bb

logger = logging.getLogger("BitBake.OE.LicenseFinder")

def get_license_md5sums(d, static_only=False, linenumbers=False):
    import bb.utils
    import csv
    md5sums = {}
    if not static_only and not linenumbers:
        # Gather md5sums of license files in common license dir
        commonlicdir = d.getVar('COMMON_LICENSE_DIR')
        for fn in os.listdir(commonlicdir):
            md5value = bb.utils.md5_file(os.path.join(commonlicdir, fn))
            md5sums[md5value] = fn

    # The following were extracted from common values in various recipes
    # (double checking the license against the license file itself, not just
    # the LICENSE value in the recipe)

    # Read license md5sums from csv file
    for path in d.getVar('BBPATH').split(':'):
        csv_path = os.path.join(path, 'files', 'license-hashes.csv')
        if os.path.isfile(csv_path):
            with open(csv_path, newline='') as csv_file:
                fieldnames = ['md5sum', 'license', 'beginline', 'endline', 'md5']
                reader = csv.DictReader(csv_file, delimiter=',', fieldnames=fieldnames)
                for row in reader:
                    if linenumbers:
                        md5sums[row['md5sum']] = (
                            row['license'], row['beginline'], row['endline'], row['md5'])
                    else:
                        md5sums[row['md5sum']] = row['license']

    return md5sums


def crunch_known_licenses(d):
    '''
    Calculate the MD5 checksums for the crunched versions of all common
    licenses. Also add additional known checksums.
    '''
    
    crunched_md5sums = {}

    # common licenses
    crunched_md5sums['ad4e9d34a2e966dfe9837f18de03266d'] = 'GFDL-1.1-only'
    crunched_md5sums['d014fb11a34eb67dc717fdcfc97e60ed'] = 'GFDL-1.2-only'
    crunched_md5sums['e020ca655b06c112def28e597ab844f1'] = 'GFDL-1.3-only'

    # The following two were gleaned from the "forever" npm package
    crunched_md5sums['0a97f8e4cbaf889d6fa51f84b89a79f6'] = 'ISC'
    # https://github.com/waffle-gl/waffle/blob/master/LICENSE.txt
    crunched_md5sums['50fab24ce589d69af8964fdbfe414c60'] = 'BSD-2-Clause'
    # https://github.com/spigwitmer/fakeds1963s/blob/master/LICENSE
    crunched_md5sums['88a4355858a1433fea99fae34a44da88'] = 'GPL-2.0-only'
    # http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt
    crunched_md5sums['063b5c3ebb5f3aa4c85a2ed18a31fbe7'] = 'GPL-2.0-only'
    # https://github.com/FFmpeg/FFmpeg/blob/master/COPYING.LGPLv2.1
    crunched_md5sums['7f5202f4d44ed15dcd4915f5210417d8'] = 'LGPL-2.1-only'
    # unixODBC-2.3.4 COPYING
    crunched_md5sums['3debde09238a8c8e1f6a847e1ec9055b'] = 'LGPL-2.1-only'
    # https://github.com/FFmpeg/FFmpeg/blob/master/COPYING.LGPLv3
    crunched_md5sums['f90c613c51aa35da4d79dd55fc724ceb'] = 'LGPL-3.0-only'
    # https://raw.githubusercontent.com/eclipse/mosquitto/v1.4.14/epl-v10
    crunched_md5sums['efe2cb9a35826992b9df68224e3c2628'] = 'EPL-1.0'

    # https://raw.githubusercontent.com/jquery/esprima/3.1.3/LICENSE.BSD
    crunched_md5sums['80fa7b56a28e8c902e6af194003220a5'] = 'BSD-2-Clause'
    # https://raw.githubusercontent.com/npm/npm-install-checks/master/LICENSE
    crunched_md5sums['e659f77bfd9002659e112d0d3d59b2c1'] = 'BSD-2-Clause'
    # https://raw.githubusercontent.com/silverwind/default-gateway/4.2.0/LICENSE
    crunched_md5sums['4c641f2d995c47f5cb08bdb4b5b6ea05'] = 'BSD-2-Clause'
    # https://raw.githubusercontent.com/tad-lispy/node-damerau-levenshtein/v1.0.5/LICENSE
    crunched_md5sums['2b8c039b2b9a25f0feb4410c4542d346'] = 'BSD-2-Clause'
    # https://raw.githubusercontent.com/terser/terser/v3.17.0/LICENSE
    crunched_md5sums['8bd23871802951c9ad63855151204c2c'] = 'BSD-2-Clause'
    # https://raw.githubusercontent.com/alexei/sprintf.js/1.0.3/LICENSE
    crunched_md5sums['008c22318c8ea65928bf730ddd0273e3'] = 'BSD-3-Clause'
    # https://raw.githubusercontent.com/Caligatio/jsSHA/v3.2.0/LICENSE
    crunched_md5sums['0e46634a01bfef056892949acaea85b1'] = 'BSD-3-Clause'
    # https://raw.githubusercontent.com/d3/d3-path/v1.0.9/LICENSE
    crunched_md5sums['b5f72aef53d3b2b432702c30b0215666'] = 'BSD-3-Clause'
    # https://raw.githubusercontent.com/feross/ieee754/v1.1.13/LICENSE
    crunched_md5sums['a39327c997c20da0937955192d86232d'] = 'BSD-3-Clause'
    # https://raw.githubusercontent.com/joyent/node-extsprintf/v1.3.0/LICENSE
    crunched_md5sums['721f23a96ff4161ca3a5f071bbe18108'] = 'MIT'
    # https://raw.githubusercontent.com/pvorb/clone/v0.2.0/LICENSE
    crunched_md5sums['b376d29a53c9573006b9970709231431'] = 'MIT'
    # https://raw.githubusercontent.com/andris9/encoding/v0.1.12/LICENSE
    crunched_md5sums['85d8a977ee9d7c5ab4ac03c9b95431c4'] = 'MIT-0'
    # https://raw.githubusercontent.com/faye/websocket-driver-node/0.7.3/LICENSE.md
    crunched_md5sums['b66384e7137e41a9b1904ef4d39703b6'] = 'Apache-2.0'
    # https://raw.githubusercontent.com/less/less.js/v4.1.1/LICENSE
    crunched_md5sums['b27575459e02221ccef97ec0bfd457ae'] = 'Apache-2.0'
    # https://raw.githubusercontent.com/microsoft/TypeScript/v3.5.3/LICENSE.txt
    crunched_md5sums['a54a1a6a39e7f9dbb4a23a42f5c7fd1c'] = 'Apache-2.0'
    # https://raw.githubusercontent.com/request/request/v2.87.0/LICENSE
    crunched_md5sums['1034431802e57486b393d00c5d262b8a'] = 'Apache-2.0'
    # https://raw.githubusercontent.com/dchest/tweetnacl-js/v0.14.5/LICENSE
    crunched_md5sums['75605e6bdd564791ab698fca65c94a4f'] = 'Unlicense'
    # https://raw.githubusercontent.com/stackgl/gl-mat3/v2.0.0/LICENSE.md
    crunched_md5sums['75512892d6f59dddb6d1c7e191957e9c'] = 'Zlib'

    commonlicdir = d.getVar('COMMON_LICENSE_DIR')
    for fn in sorted(os.listdir(commonlicdir)):
        md5value, lictext = crunch_license(os.path.join(commonlicdir, fn))
        if md5value not in crunched_md5sums:
            crunched_md5sums[md5value] = fn
        elif fn != crunched_md5sums[md5value]:
            bb.debug(2, "crunched_md5sums['%s'] is already set to '%s' rather than '%s'" % (md5value, crunched_md5sums[md5value], fn))
        else:
            bb.debug(2, "crunched_md5sums['%s'] is already set to '%s'" % (md5value, crunched_md5sums[md5value]))

    return crunched_md5sums


def crunch_license(licfile):
    '''
    Remove non-material text from a license file and then calculate its
    md5sum. This works well for licenses that contain a copyright statement,
    but is also a useful way to handle people's insistence upon reformatting
    the license text slightly (with no material difference to the text of the
    license).
    '''

    import oe.utils

    # Note: these are carefully constructed!
    license_title_re = re.compile(r'^#*\(? *(This is )?([Tt]he )?.{0,15} ?[Ll]icen[sc]e( \(.{1,10}\))?\)?[:\.]? ?#*$')
    license_statement_re = re.compile(r'^((This (project|software)|.{1,10}) is( free software)? (released|licen[sc]ed)|(Released|Licen[cs]ed)) under the .{1,10} [Ll]icen[sc]e:?$')
    copyright_re = re.compile(r'^ *[#\*]* *(Modified work |MIT LICENSED )?Copyright ?(\([cC]\))? .*$')
    disclaimer_re = re.compile(r'^ *\*? ?All [Rr]ights [Rr]eserved\.$')
    email_re = re.compile(r'^.*<[\w\.-]*@[\w\.\-]*>$')
    header_re = re.compile(r'^(\/\**!?)? ?[\-=\*]* ?(\*\/)?$')
    tag_re = re.compile(r'^ *@?\(?([Ll]icense|MIT)\)?$')
    url_re = re.compile(r'^ *[#\*]* *https?:\/\/[\w\.\/\-]+$')

    lictext = []
    with open(licfile, 'r', errors='surrogateescape') as f:
        for line in f:
            # Drop opening statements
            if copyright_re.match(line):
                continue
            elif disclaimer_re.match(line):
                continue
            elif email_re.match(line):
                continue
            elif header_re.match(line):
                continue
            elif tag_re.match(line):
                continue
            elif url_re.match(line):
                continue
            elif license_title_re.match(line):
                continue
            elif license_statement_re.match(line):
                continue
            # Strip comment symbols
            line = line.replace('*', '') \
                       .replace('#', '')
            # Unify spelling
            line = line.replace('sub-license', 'sublicense')
            # Squash spaces
            line = oe.utils.squashspaces(line.strip())
            # Replace smart quotes, double quotes and backticks with single quotes
            line = line.replace(u"\u2018", "'").replace(u"\u2019", "'").replace(u"\u201c","'").replace(u"\u201d", "'").replace('"', '\'').replace('`', '\'')
            # Unify brackets
            line = line.replace("{", "[").replace("}", "]")
            if line:
                lictext.append(line)

    m = hashlib.md5()
    try:
        m.update(' '.join(lictext).encode('utf-8'))
        md5val = m.hexdigest()
    except UnicodeEncodeError:
        md5val = None
        lictext = ''
    return md5val, lictext


def find_license_files(srctree, first_only=False):
    """
    Search srctree for files that look like they could be licenses.
    If first_only is True, only return the first file found.
    """
    licspecs = ['*LICEN[CS]E*', 'COPYING*', '*[Ll]icense*', 'LEGAL*', '[Ll]egal*', '*GPL*', 'README.lic*', 'COPYRIGHT*', '[Cc]opyright*', 'e[dp]l-v10']
    skip_extensions = (".html", ".js", ".json", ".svg", ".ts", ".go", ".sh")
    licfiles = []
    for root, dirs, files in os.walk(srctree):
        # Sort files so that LICENSE is before LICENSE.subcomponent, which is
        # meaningful if first_only is set.
        for fn in sorted(files):
            if fn.endswith(skip_extensions):
                continue
            for spec in licspecs:
                if fnmatch.fnmatch(fn, spec):
                    fullpath = os.path.join(root, fn)
                    if not fullpath in licfiles:
                        licfiles.append(fullpath)
                        if first_only:
                            return licfiles

    return licfiles


def match_licenses(licfiles, srctree, d):
    import bb
    md5sums = get_license_md5sums(d)

    crunched_md5sums = crunch_known_licenses(d)

    licenses = []
    for licfile in sorted(licfiles):
        resolved_licfile = d.expand(licfile)
        md5value = bb.utils.md5_file(resolved_licfile)
        license = md5sums.get(md5value, None)
        if not license:
            crunched_md5, lictext = crunch_license(resolved_licfile)
            license = crunched_md5sums.get(crunched_md5, None)
            if lictext and not license:
                license = 'Unknown'
                logger.info("Please add the following line for '%s' to a 'license-hashes.csv' " \
                    "and replace `Unknown` with the license:\n" \
                    "%s,Unknown" % (os.path.relpath(licfile, srctree + "/.."), md5value))
        if license:
            licenses.append((license, os.path.relpath(licfile, srctree), md5value))

    return licenses


def find_licenses(srctree, d, first_only=False):
    licfiles = find_license_files(srctree, first_only)
    licenses = match_licenses(licfiles, srctree, d)

    # FIXME should we grab at least one source file with a license header and add that too?

    return licenses
