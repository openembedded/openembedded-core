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
