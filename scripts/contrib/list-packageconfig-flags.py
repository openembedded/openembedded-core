#!/usr/bin/env python

# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software Foundation.
#
# Copyright (C) 2013 Wind River Systems, Inc.
#
# - list available pkgs which have PACKAGECONFIG flags
# - list available PACKAGECONFIG flags and all affected pkgs
# - list all pkgs and PACKAGECONFIG information

import sys
import getopt
import os

def search_bitbakepath():
    bitbakepath = ""

    # Search path to bitbake lib dir in order to load bb modules
    if os.path.exists(os.path.join(os.path.dirname(sys.argv[0]), '../../bitbake/lib/bb')):
        bitbakepath = os.path.join(os.path.dirname(sys.argv[0]), '../../bitbake/lib')
        bitbakepath = os.path.abspath(bitbakepath)
    else:
        # Look for bitbake/bin dir in PATH
        for pth in os.environ['PATH'].split(':'):
            if os.path.exists(os.path.join(pth, '../lib/bb')):
                bitbakepath = os.path.abspath(os.path.join(pth, '../lib'))
                break
        if not bitbakepath:
            sys.stderr.write("Unable to find bitbake by searching parent directory of this script or PATH\n")
            sys.exit(1)
    return bitbakepath

# For importing the following modules
sys.path.insert(0, search_bitbakepath())
import bb.cache
import bb.cooker
import bb.providers
import bb.tinfoil

usage_body = '''  list available pkgs which have PACKAGECONFIG flags

OPTION:
  -h, --help    display this help and exit
  -f, --flag    list available PACKAGECONFIG flags and all affected pkgs
  -a, --all     list all pkgs and PACKAGECONFIG information
  -p, --prefer  list pkgs with preferred version

EXAMPLE:
list-packageconfig-flags.py
list-packageconfig-flags.py -f
list-packageconfig-flags.py -a
list-packageconfig-flags.py -p
list-packageconfig-flags.py -f -p
list-packageconfig-flags.py -a -p
'''

def usage():
    print 'Usage: %s [-f|-a] [-p]' % os.path.basename(sys.argv[0])
    print usage_body

def get_fnlist(bbhandler, pkg_pn, preferred):
    ''' Get all recipe file names '''
    if preferred:
        (latest_versions, preferred_versions) = bb.providers.findProviders(bbhandler.config_data, bbhandler.cooker.recipecache, pkg_pn)

    fn_list = []
    for pn in sorted(pkg_pn):
        if preferred:
            fn_list.append(preferred_versions[pn][1])
        else:
            fn_list.extend(pkg_pn[pn])

    return fn_list

def get_recipesdata(bbhandler, preferred):
    ''' Get data of all available recipes which have PACKAGECONFIG flags '''
    pkg_pn = bbhandler.cooker.recipecache.pkg_pn

    data_dict = {}
    for fn in get_fnlist(bbhandler, pkg_pn, preferred):
        data = bb.cache.Cache.loadDataFull(fn, bbhandler.cooker.collection.get_file_appends(fn), bbhandler.config_data)
        if data.getVarFlags("PACKAGECONFIG"):
            data_dict[fn] = data

    return data_dict

def collect_pkgs(data_dict):
    ''' Collect available pkgs in which have PACKAGECONFIG flags '''
    # pkg_dict = {'pkg1': ['flag1', 'flag2',...]}
    pkg_dict = {}
    for fn in data_dict:
        pkgconfigflags = data_dict[fn].getVarFlags("PACKAGECONFIG")
        pkgname = data_dict[fn].getVar("P", True)
        pkg_dict[pkgname] = sorted(pkgconfigflags.keys())

    return pkg_dict

def collect_flags(pkg_dict):
    ''' Collect available PACKAGECONFIG flags and all affected pkgs '''
    # flag_dict = {'flag': ['pkg1', 'pkg2',...]}
    flag_dict = {}
    for pkgname, flaglist in pkg_dict.iteritems():
        for flag in flaglist:
            if flag == "defaultval":
                continue

            if flag in flag_dict:
                flag_dict[flag].append(pkgname)
            else:
                flag_dict[flag] = [pkgname]

    return flag_dict

def display_pkgs(pkg_dict):
    ''' Display available pkgs which have PACKAGECONFIG flags '''
    pkgname_len = len("PACKAGE NAME") + 1
    for pkgname in pkg_dict:
        if pkgname_len < len(pkgname):
            pkgname_len = len(pkgname)
    pkgname_len += 1

    header = '%-*s%s' % (pkgname_len, str("PACKAGE NAME"), str("PACKAGECONFIG FLAGS"))
    print header
    print str("").ljust(len(header), '=')
    for pkgname in sorted(pkg_dict):
        print('%-*s%s' % (pkgname_len, pkgname, ' '.join(pkg_dict[pkgname])))


def display_flags(flag_dict):
    ''' Display available PACKAGECONFIG flags and all affected pkgs '''
    flag_len = len("PACKAGECONFIG FLAG") + 5

    header = '%-*s%s' % (flag_len, str("PACKAGECONFIG FLAG"), str("PACKAGE NAMES"))
    print header
    print str("").ljust(len(header), '=')

    for flag in sorted(flag_dict):
        print('%-*s%s' % (flag_len, flag, '  '.join(sorted(flag_dict[flag]))))

def display_all(data_dict):
    ''' Display all pkgs and PACKAGECONFIG information '''
    print str("").ljust(50, '=')
    for fn in data_dict:
        print('%s' % data_dict[fn].getVar("P", True))
        print fn
        packageconfig = data_dict[fn].getVar("PACKAGECONFIG", True) or ''
        if packageconfig.strip() == '':
            packageconfig = 'None'
        print('PACKAGECONFIG %s' % packageconfig)

        for flag,flag_val in data_dict[fn].getVarFlags("PACKAGECONFIG").iteritems():
            if flag == "defaultval":
                continue
            print('PACKAGECONFIG[%s] %s' % (flag, flag_val))
        print ''

def main():
    listtype = 'pkgs'
    preferred = False
    pkg_dict = {}
    flag_dict = {}

    # Collect and validate input
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hfap", ["help", "flag", "all", "prefer"])
    except getopt.GetoptError, err:
        print >> sys.stderr,'%s' % str(err)
        usage()
        sys.exit(2)
    for opt, value in opts:
        if opt in ('-h', '--help'):
            usage()
            sys.exit(0)
        elif opt in ('-f', '--flag'):
            listtype = 'flags'
        elif opt in ('-a', '--all'):
            listtype = 'all'
        elif opt in ('-p', '--prefer'):
            preferred = True
        else:
            assert False, "unhandled option"

    bbhandler = bb.tinfoil.Tinfoil()
    bbhandler.prepare()
    data_dict = get_recipesdata(bbhandler, preferred)

    if listtype == 'flags':
        pkg_dict = collect_pkgs(data_dict)
        flag_dict = collect_flags(pkg_dict)
        display_flags(flag_dict)
    elif listtype == 'pkgs':
        pkg_dict = collect_pkgs(data_dict)
        display_pkgs(pkg_dict)
    elif listtype == 'all':
        display_all(data_dict)

if __name__ == "__main__":
    main()
