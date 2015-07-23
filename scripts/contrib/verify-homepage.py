#!/usr/bin/env python

# This script is used for verify HOMEPAGE.
# The result is influenced by network environment, since the timeout of connect url is 5 seconds as default.

import sys
import os
import subprocess
import urllib2


# Allow importing scripts/lib modules
scripts_path = os.path.abspath(os.path.dirname(os.path.realpath(__file__)) + '/..')
lib_path = scripts_path + '/lib'
sys.path = sys.path + [lib_path]
import scriptpath

# Allow importing bitbake modules
bitbakepath = scriptpath.add_bitbake_lib_path()

import bb.tinfoil

def wgetHomepage(pn, homepage):
    result = subprocess.call('wget ' + '-q -T 5 -t 1 --spider ' + homepage, shell = True)
    if result:
        bb.warn("Failed to verify HOMEPAGE (%s) of %s" % (homepage, pn))
        return 1
    else:
        return 0

def verifyHomepage(bbhandler):
    pkg_pn = bbhandler.cooker.recipecache.pkg_pn
    pnlist = sorted(pkg_pn)
    count = 0
    for pn in pnlist:
        fn = pkg_pn[pn].pop()
        data = bb.cache.Cache.loadDataFull(fn, bbhandler.cooker.collection.get_file_appends(fn), bbhandler.config_data)
        homepage = data.getVar("HOMEPAGE", True)
        if homepage:
            try:
                urllib2.urlopen(homepage, timeout=5)
            except Exception:
                count = count + wgetHomepage(pn, homepage)
    return count

if __name__=='__main__':
    failcount = 0
    bbhandler = bb.tinfoil.Tinfoil()
    bbhandler.prepare()
    print "Start to verify HOMEPAGE:"
    failcount = verifyHomepage(bbhandler)
    print "finish to verify HOMEPAGE."
    print "Summary: %s failed" % failcount
