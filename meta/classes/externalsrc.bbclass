# Copyright (C) 2012 Linux Foundation
# Author: Richard Purdie
# Some code and influence taken from srctree.bbclass:
# Copyright (C) 2009 Chris Larson <clarson@kergoth.com>
# Released under the MIT license (see COPYING.MIT for the terms)
#
# externalsrc.bbclass enables use of an existing source tree, usually external to 
# the build system to build a piece of software rather than the usual fetch/unpack/patch
# process.
#
# To use, add externalsrc to the global inherit and set EXTERNALSRC to point at the
# directory you want to use containing the sources e.g. from local.conf for a recipe
# called "myrecipe" you would do:
#
# INHERIT += "externalsrc"
# EXTERNALSRC_pn-myrecipe = "/path/to/my/source/tree"
#
# In order to make this class work for both target and native versions (or with
# multilibs/cross or other BBCLASSEXTEND variants), B is set to point to a separate
# directory under the work directory (split source and build directories). This is
# the default, but the build directory can be set to the source directory if
# circumstances dictate by setting EXTERNALSRC_BUILD to the same value, e.g.:
#
# EXTERNALSRC_BUILD_pn-myrecipe = "/path/to/my/source/tree"
#

SRCTREECOVEREDTASKS ?= "do_patch do_unpack do_fetch"

python () {
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        d.setVar('S', externalsrc)
        externalsrcbuild = d.getVar('EXTERNALSRC_BUILD', True)
        if externalsrcbuild:
            d.setVar('B', externalsrcbuild)
        else:
            d.setVar('B', '${WORKDIR}/${BPN}-${PV}/')
        d.setVar('SRC_URI', '')

        tasks = filter(lambda k: d.getVarFlag(k, "task"), d.keys())

        for task in tasks:
            if task.endswith("_setscene"):
                # sstate is never going to work for external source trees, disable it
                bb.build.deltask(task, d)
            else:
                # Since configure will likely touch ${S}, ensure only we lock so one task has access at a time
                d.appendVarFlag(task, "lockfiles", "${S}/singletask.lock")

        for task in d.getVar("SRCTREECOVEREDTASKS", True).split():
            bb.build.deltask(task, d)
}

