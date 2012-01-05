# Report significant differences in the buildhistory repository since a specific revision
#
# Copyright (C) 2012 Intel Corporation
# Author: Paul Eggleton <paul.eggleton@linux.intel.com>
#
# Note: requires GitPython 0.3.1+
#
# You can use this from the command line by running scripts/buildhistory-diff
#

import sys
import os.path
import difflib
import git


# How to display fields
pkg_list_fields = ['DEPENDS', 'RDEPENDS', 'RRECOMMENDS', 'PACKAGES', 'FILES', 'FILELIST']
pkg_numeric_fields = ['PKGSIZE']
# Fields to monitor
pkg_monitor_fields = ['RDEPENDS', 'RRECOMMENDS', 'PACKAGES', 'FILELIST', 'PKGSIZE']
# Percentage change to alert for numeric fields
pkg_monitor_numeric_threshold = 20
# Image files to monitor
img_monitor_files = ['installed-package-names.txt', 'files-in-image.txt']


class ChangeRecord:
    def __init__(self, path, fieldname, oldvalue, newvalue):
        self.path = path
        self.fieldname = fieldname
        self.oldvalue = oldvalue
        self.newvalue = newvalue
        self.filechanges = None

    def __str__(self):
        if self.fieldname in pkg_list_fields:
            aitems = self.oldvalue.split(' ')
            bitems = self.newvalue.split(' ')
            removed = list(set(aitems) - set(bitems))
            added = list(set(bitems) - set(aitems))
            return '%s: %s:%s%s' % (self.path, self.fieldname, ' removed "%s"' % ' '.join(removed) if removed else '', ' added "%s"' % ' '.join(added) if added else '')
        elif self.fieldname in pkg_numeric_fields:
            aval = int(self.oldvalue)
            bval = int(self.newvalue)
            percentchg = ((bval - aval) / float(aval)) * 100
            return '%s: %s changed from %d to %d (%s%d%%)' % (self.path, self.fieldname, aval, bval, '+' if percentchg > 0 else '', percentchg)
        elif self.fieldname in img_monitor_files:
            out = 'Changes to %s (%s):\n  ' % (self.path, self.fieldname)
            if self.filechanges:
                out += '\n  '.join(['%s' % i for i in self.filechanges])
            else:
                alines = self.oldvalue.splitlines()
                blines = self.newvalue.splitlines()
                diff = difflib.unified_diff(alines, blines, self.fieldname, self.fieldname, lineterm='')
                out += '\n  '.join(list(diff))
                out += '\n  --'
            return out
        else:
            return '%s: %s changed from "%s" to "%s"' % (self.path, self.self.fieldname, self.oldvalue, self.newvalue)


class FileChange:
    changetype_add = 'A'
    changetype_remove = 'R'
    changetype_type = 'T'
    changetype_perms = 'P'
    changetype_ownergroup = 'O'
    changetype_link = 'L'

    def __init__(self, path, changetype, oldvalue = None, newvalue = None):
        self.path = path
        self.changetype = changetype
        self.oldvalue = oldvalue
        self.newvalue = newvalue

    def _ftype_str(self, ftype):
        if ftype == '-':
            return 'file'
        elif ftype == 'd':
            return 'directory'
        elif ftype == 'l':
            return 'symlink'
        elif ftype == 'c':
            return 'char device'
        elif ftype == 'b':
            return 'block device'
        elif ftype == 'p':
            return 'fifo'
        elif ftype == 's':
            return 'socket'
        else:
            return 'unknown (%s)' % ftype

    def __str__(self):
        if self.changetype == self.changetype_add:
            return '%s was added' % self.path
        elif self.changetype == self.changetype_remove:
            return '%s was removed' % self.path
        elif self.changetype == self.changetype_type:
            return '%s changed type from %s to %s' % (self.path, self._ftype_str(self.oldvalue), self._ftype_str(self.newvalue))
        elif self.changetype == self.changetype_perms:
            return '%s changed permissions from %s to %s' % (self.path, self.oldvalue, self.newvalue)
        elif self.changetype == self.changetype_ownergroup:
            return '%s changed owner/group from %s to %s' % (self.path, self.oldvalue, self.newvalue)
        elif self.changetype == self.changetype_link:
            return '%s changed symlink target from %s to %s' % (self.path, self.oldvalue, self.newvalue)
        else:
            return '%s changed (unknown)' % self.path


def blob_to_dict(blob):
    alines = blob.data_stream.read().splitlines()
    adict = {}
    for line in alines:
        splitv = [i.strip() for i in line.split('=',1)]
        if splitv.count > 1:
            adict[splitv[0]] = splitv[1]
    return adict


def file_list_to_dict(lines):
    adict = {}
    for line in lines:
        # Leave the last few fields intact so we handle file names containing spaces
        splitv = line.split(None,4)
        # Grab the path and remove the leading .
        path = splitv[4][1:].strip()
        # Handle symlinks
        if(' -> ' in path):
            target = path.split(' -> ')[1]
            path = path.split(' -> ')[0]
            adict[path] = splitv[0:3] + [target]
        else:
            adict[path] = splitv[0:3]
    return adict


def compare_file_lists(alines, blines):
    adict = file_list_to_dict(alines)
    bdict = file_list_to_dict(blines)
    filechanges = []
    for path, splitv in adict.iteritems():
        newsplitv = bdict.pop(path, None)
        if newsplitv:
            # Check type
            oldvalue = splitv[0][0]
            newvalue = newsplitv[0][0]
            if oldvalue != newvalue:
                filechanges.append(FileChange(path, FileChange.changetype_type, oldvalue, newvalue))
            # Check permissions
            oldvalue = splitv[0][1:]
            newvalue = newsplitv[0][1:]
            if oldvalue != newvalue:
                filechanges.append(FileChange(path, FileChange.changetype_perms, oldvalue, newvalue))
            # Check owner/group
            oldvalue = '%s/%s' % (splitv[1], splitv[2])
            newvalue = '%s/%s' % (newsplitv[1], newsplitv[2])
            if oldvalue != newvalue:
                filechanges.append(FileChange(path, FileChange.changetype_ownergroup, oldvalue, newvalue))
            # Check symlink target
            if newsplitv[0][0] == 'l':
                if splitv.count > 3:
                    oldvalue = splitv[3]
                else:
                    oldvalue = None
                newvalue = newsplitv[3]
                if oldvalue != newvalue:
                    filechanges.append(FileChange(path, FileChange.changetype_link, oldvalue, newvalue))
        else:
            filechanges.append(FileChange(path, FileChange.changetype_remove))

    # Whatever is left over has been added
    for path in bdict:
        filechanges.append(FileChange(path, FileChange.changetype_add))

    return filechanges


def compare_lists(alines, blines):
    removed = list(set(alines) - set(blines))
    added = list(set(blines) - set(alines))

    filechanges = []
    for pkg in removed:
        filechanges.append(FileChange(pkg, FileChange.changetype_remove))
    for pkg in added:
        filechanges.append(FileChange(pkg, FileChange.changetype_add))

    return filechanges


def process_changes(repopath, revision1, revision2 = 'HEAD', report_all = False):
    repo = git.Repo(repopath)
    assert repo.bare == False
    commit = repo.commit(revision1)
    diff = commit.diff(revision2)

    changes = []
    for d in diff.iter_change_type('M'):
        path = os.path.dirname(d.a_blob.path)
        if path.startswith('packages/'):
            adict = blob_to_dict(d.a_blob)
            bdict = blob_to_dict(d.b_blob)

            for key in adict:
                if report_all or key in pkg_monitor_fields:
                    if adict[key] != bdict[key]:
                        if (not report_all) and key in pkg_numeric_fields:
                            aval = int(adict[key])
                            bval = int(bdict[key])
                            percentchg = ((bval - aval) / float(aval)) * 100
                            if percentchg < pkg_monitor_numeric_threshold:
                                continue
                        chg = ChangeRecord(path, key, adict[key], bdict[key])
                        changes.append(chg)
        elif path.startswith('images/'):
            filename = os.path.basename(d.a_blob.path)
            if filename in img_monitor_files:
                if filename == 'files-in-image.txt':
                    alines = d.a_blob.data_stream.read().splitlines()
                    blines = d.b_blob.data_stream.read().splitlines()
                    filechanges = compare_file_lists(alines,blines)
                    if filechanges:
                        chg = ChangeRecord(path, filename, None, None)
                        chg.filechanges = filechanges
                        changes.append(chg)
                elif filename == 'installed-package-names.txt':
                    alines = d.a_blob.data_stream.read().splitlines()
                    blines = d.b_blob.data_stream.read().splitlines()
                    filechanges = compare_lists(alines,blines)
                    if filechanges:
                        chg = ChangeRecord(path, filename, None, None)
                        chg.filechanges = filechanges
                        changes.append(chg)
                else:
                    chg = ChangeRecord(path, filename, d.a_blob.data_stream.read(), d.b_blob.data_stream.read())
                    changes.append(chg)

    return changes
