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
# To use, set S to point at the directory you want to use containing the sources
# e.g. S = "/path/to/my/source/tree"
#
# If the class is to work for both target and native versions (or with multilibs/
# cross or other BBCLASSEXTEND variants), its expected that setting B to point to 
# where to place the compiled binaries will work (split source and build directories).
# This is the default but B can be set to S if circumstaces dictate.
#

SRC_URI = ""
SRCTREECOVEREDTASKS ?= "do_patch do_unpack do_fetch"
B = "${WORKDIR}/${BPN}-${PV}/"

def remove_tasks(tasks, deltasks, d):
    for task in tasks:
        deps = d.getVarFlag(task, "deps")
        for preptask in deltasks:
            if preptask in deps:
                deps.remove(preptask)
        d.setVarFlag(task, "deps", deps)
    # Poking around bitbake internal variables is evil but there appears to be no better way :(
    tasklist = d.getVar('__BBTASKS') or []
    for task in deltasks:
        d.delVarFlag(task, "task")
        if task in tasklist:
            tasklist.remove(task)
    d.setVar('__BBTASKS', tasklist)

python () {
    tasks = filter(lambda k: d.getVarFlag(k, "task"), d.keys())
    covered = d.getVar("SRCTREECOVEREDTASKS", True).split()

    for task in tasks:
        if task.endswith("_setscene"):
            # sstate is never going to work for external source trees, disable it
            covered.append(task)
        else:
            # Since configure will likely touch ${S}, ensure only we lock so one task has access at a time
            d.appendVarFlag(task, "lockfiles", "${S}/singletask.lock")

    remove_tasks(tasks, covered, d)
}

