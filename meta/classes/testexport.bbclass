# Copyright (C) 2016 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)
#
#
# testexport.bbclass allows to execute runtime test outside OE environment.
# Most of the tests are commands run on target image over ssh.
# To use it add testexport to global inherit and call your target image with -c testexport
# You can try it out like this:
# - First build an image. i.e. core-image-sato
# - Add INHERIT += "testexport" in local.conf
# - Then bitbake core-image-sato -c testexport. That will generate the directory structure
#   to execute the runtime tests using runexported.py.
#
# For more information on TEST_SUITES check testimage class.

TEST_LOG_DIR ?= "${WORKDIR}/testexport"
TEST_EXPORT_DIR ?= "${TMPDIR}/testexport/${PN}"
TEST_TARGET ?= "simpleremote"
TEST_TARGET_IP ?= ""
TEST_SERVER_IP ?= ""

TEST_EXPORT_DEPENDS = ""
TEST_EXPORT_LOCK = "${TMPDIR}/testimage.lock"

python do_testexport() {
    testexport_main(d)
}

addtask testexport
do_testimage[nostamp] = "1"
do_testimage[depends] += "${TEST_EXPORT_DEPENDS}"
do_testimage[lockfiles] += "${TEST_EXPORT_LOCK}"

def exportTests(d,tc):
    import json
    import shutil
    import pkgutil
    import re

    exportpath = d.getVar("TEST_EXPORT_DIR", True)

    savedata = {}
    savedata["d"] = {}
    savedata["target"] = {}
    for key in tc.__dict__:
        # special cases
        if key not in ['d', 'target', 'suite']:
            savedata[key] = getattr(tc, key)
    savedata["target"]["ip"] = tc.target.ip or d.getVar("TEST_TARGET_IP", True)
    savedata["target"]["server_ip"] = tc.target.server_ip or d.getVar("TEST_SERVER_IP", True)

    keys = [ key for key in d.keys() if not key.startswith("_") and not key.startswith("BB") \
            and not key.startswith("B_pn") and not key.startswith("do_") and not d.getVarFlag(key, "func", True)]
    for key in keys:
        try:
            savedata["d"][key] = d.getVar(key, True)
        except bb.data_smart.ExpansionError:
            # we don't care about those anyway
            pass

    json_file = os.path.join(exportpath, "testdata.json")
    with open(json_file, "w") as f:
            json.dump(savedata, f, skipkeys=True, indent=4, sort_keys=True)

    # Replace absolute path with relative in the file
    exclude_path = os.path.join(d.getVar("COREBASE", True),'meta','lib','oeqa')
    f1 = open(json_file,'r').read()
    f2 = open(json_file,'w')
    m = f1.replace(exclude_path,'oeqa')
    f2.write(m)
    f2.close()

    # now start copying files
    # we'll basically copy everything under meta/lib/oeqa, with these exceptions
    #  - oeqa/targetcontrol.py - not needed
    #  - oeqa/selftest - something else
    # That means:
    #   - all tests from oeqa/runtime defined in TEST_SUITES (including from other layers)
    #   - the contents of oeqa/utils and oeqa/runtime/files
    #   - oeqa/oetest.py and oeqa/runexport.py (this will get copied to exportpath not exportpath/oeqa)
    #   - __init__.py files
    bb.utils.mkdirhier(os.path.join(exportpath, "oeqa/runtime/files"))
    bb.utils.mkdirhier(os.path.join(exportpath, "oeqa/utils"))
    # copy test modules, this should cover tests in other layers too
    bbpath = d.getVar("BBPATH", True).split(':')
    for t in tc.testslist:
        isfolder = False
        if re.search("\w+\.\w+\.test_\S+", t):
            t = '.'.join(t.split('.')[:3])
        mod = pkgutil.get_loader(t)
        # More depth than usual?
        if (t.count('.') > 2):
            for p in bbpath:
                foldername = os.path.join(p, 'lib',  os.sep.join(t.split('.')).rsplit(os.sep, 1)[0])
                if os.path.isdir(foldername):
                    isfolder = True
                    target_folder = os.path.join(exportpath, "oeqa", "runtime", os.path.basename(foldername))
                    if not os.path.exists(target_folder):
                        shutil.copytree(foldername, target_folder)
        if not isfolder:
            shutil.copy2(mod.filename, os.path.join(exportpath, "oeqa/runtime"))
    # copy __init__.py files
    oeqadir = pkgutil.get_loader("oeqa").filename
    shutil.copy2(os.path.join(oeqadir, "__init__.py"), os.path.join(exportpath, "oeqa"))
    shutil.copy2(os.path.join(oeqadir, "runtime/__init__.py"), os.path.join(exportpath, "oeqa/runtime"))
    # copy oeqa/oetest.py and oeqa/runexported.py
    shutil.copy2(os.path.join(oeqadir, "oetest.py"), os.path.join(exportpath, "oeqa"))
    shutil.copy2(os.path.join(oeqadir, "runexported.py"), exportpath)
    # copy oeqa/utils/*.py
    for root, dirs, files in os.walk(os.path.join(oeqadir, "utils")):
        for f in files:
            if f.endswith(".py"):
                shutil.copy2(os.path.join(root, f), os.path.join(exportpath, "oeqa/utils"))
    # copy oeqa/runtime/files/*
    for root, dirs, files in os.walk(os.path.join(oeqadir, "runtime/files")):
        for f in files:
            shutil.copy2(os.path.join(root, f), os.path.join(exportpath, "oeqa/runtime/files"))

    bb.plain("Exported tests to: %s" % exportpath)

def testexport_main(d):
    from oeqa.oetest import ExportTestContext
    from oeqa.targetcontrol import get_target_controller
    from oeqa.utils.dump import get_host_dumper

    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR", True))
    bb.utils.remove(d.getVar("TEST_EXPORT_DIR", True), recurse=True)
    bb.utils.mkdirhier(d.getVar("TEST_EXPORT_DIR", True))

    # the robot dance
    target = get_target_controller(d)

    # test context
    tc = ExportTestContext(d, target)

    # this is a dummy load of tests
    # we are doing that to find compile errors in the tests themselves
    # before booting the image
    try:
        tc.loadTests()
    except Exception as e:
        import traceback
        bb.fatal("Loading tests failed:\n%s" % traceback.format_exc())

    exportTests(d,tc)

testexport_main[vardepsexclude] =+ "BB_ORIGENV"

inherit testimage
