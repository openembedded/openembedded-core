# Test related variables
# By default, TEST_DIR is created under WORKDIR
TEST_DIR ?= "${WORKDIR}/qemuimagetest"
TEST_LOG ?= "${LOG_DIR}/qemuimagetests"
TEST_RESULT ?= "${TEST_DIR}/result"
TEST_TMP ?= "${TEST_DIR}/tmp"
TEST_SCEN ?= "sanity"

python do_qemuimagetest() {
    qemuimagetest_main(d)
}
addtask qemuimagetest before do_build after do_rootfs
do_qemuimagetest[nostamp] = "1"
do_qemuimagetest[depends] += "qemu-native:do_populate_sysroot"

python do_qemuimagetest_standalone() {
    qemuimagetest_main(d)
}
addtask qemuimagetest_standalone
do_qemuimagetest_standalone[nostamp] = "1"
do_qemuimagetest_standalone[depends] += "qemu-native:do_populate_sysroot"

def qemuimagetest_main(d):
    import sys
    import re
    import os
    
    """
    Test Controller for Poky Testing.
    """
    
    casestr = re.compile(r'(?P<scen>\w+\b):(?P<case>\w+$)')
    resultstr = re.compile(r'\s*(?P<case>\w+)\s*(?P<pass>\d+)\s*(?P<fail>\d+)\s*(?P<noresult>\d+)')
    machine = bb.data.getVar('MACHINE', d, 1)
    pname = bb.data.getVar('PN', d, 1)
    
    """funtion to run each case under scenario"""
    def runtest(scen, case, fulltestpath):
        resultpath = bb.data.getVar('TEST_RESULT', d, 1)
        testpath = bb.data.getVar('TEST_DIR', d, 1)

        """initialize log file for testcase"""
        logpath = bb.data.getVar('TEST_LOG', d, 1)
        bb.utils.mkdirhier("%s/%s" % (logpath, scen))
        caselog = os.path.join(logpath, "%s/log_%s.%s" % (scen, case, bb.data.getVar('DATETIME', d, 1)))
        os.system("touch %s" % caselog)
        

        """export TEST_TMP, TEST_RESULT, DEPLOY_DIR and QEMUARCH"""
        os.environ["PATH"] = bb.data.getVar("PATH", d, True)
        os.environ["TEST_TMP"] = testpath
        os.environ["TEST_RESULT"] = resultpath
        os.environ["DEPLOY_DIR"] = bb.data.getVar("DEPLOY_DIR", d, True)
        os.environ["QEMUARCH"] = machine
        os.environ["QEMUTARGET"] = pname
        os.environ["DISPLAY"] = bb.data.getVar("DISPLAY", d, True)
        os.environ["POKYBASE"] = bb.data.getVar("POKYBASE", d, True)
        os.environ["TOPDIR"] = bb.data.getVar("TOPDIR", d, True)

        """run Test Case"""
        bb.note("Run %s test in scenario %s" % (case, scen))
        os.system("%s | tee -a %s" % (fulltestpath, caselog))
    
    """Generate testcase list in runtime"""
    def generate_list(testlist):
        list = []
        if len(testlist) == 0:
            raise bb.build.FuncFailed("No testcase defined in TEST_SCEN")

        """check testcase folder and add case list according to TEST_SCEN"""
        for item in testlist.split(" "):
            n = casestr.match(item)
            if n:
                item = n.group('scen')
                casefile = n.group('case')
                for dir in bb.data.getVar("QEMUIMAGETESTS", d, True).split():
                    fulltestcase = os.path.join(dir, item, casefile)
                    if not os.path.isfile(fulltestcase):
                        raise bb.build.FuncFailed("Testcase %s not found" % fulltestcase)
                    list.append((item, casefile, fulltestcase))
            else:
                for dir in bb.data.getVar("QEMUIMAGETESTS", d, True).split():
                    scenlist = os.path.join(dir, "scenario", machine, pname)
                    if not os.path.isfile(scenlist):
                        raise bb.build.FuncFailed("No scenario list file named %s found" % scenlist)

                    f = open(scenlist, "r")
                    for line in f:
                       if item != line.split()[0]:
                           continue
                       else:
                           casefile = line.split()[1]

                       fulltestcase = os.path.join(dir, item, casefile)
                       if not os.path.isfile(fulltestcase):
                            raise bb.build.FuncFailed("Testcase %s not found" % fulltestcase)
                       list.append((item, casefile, fulltestcase))
        return list

    """check testcase folder and create test log folder"""
    testpath = bb.data.getVar('TEST_DIR', d, 1)
    bb.utils.mkdirhier(testpath)
    
    logpath = bb.data.getVar('TEST_LOG', d, 1)
    bb.utils.mkdirhier(logpath)

    tmppath = bb.data.getVar('TEST_TMP', d, 1)
    bb.utils.mkdirhier(tmppath)
    
    """initialize result file"""
    resultpath = bb.data.getVar('TEST_RESULT', d, 1)
    bb.utils.mkdirhier(resultpath)
    resultfile = os.path.join(resultpath, "testresult.%s" % bb.data.getVar('DATETIME', d, 1))
    sresultfile = os.path.join(resultpath, "testresult.log")

    machine = bb.data.getVar('MACHINE', d, 1)

    if os.path.exists(sresultfile):
        os.remove(sresultfile)
    os.system("touch %s" % resultfile)
    os.symlink(resultfile, sresultfile)
    f = open(sresultfile, "a")
    f.write("\tTest Result for %s\n" % machine)
    f.write("\t%-15s%-15s%-15s%-15s\n" % ("Testcase", "PASS", "FAIL", "NORESULT"))
    f.close()
    
    """generate pre-defined testcase list"""
    testlist = bb.data.getVar('TEST_SCEN', d, 1)
    fulllist = generate_list(testlist)

    """Begin testing"""
    for test in fulllist:
        (scen, case, fullpath) = test
        runtest(scen, case, fullpath)
    
    """Print Test Result"""
    ret = 0
    f = open(sresultfile, "r")
    for line in f:
        m = resultstr.match(line)
        if m:
            if m.group('fail') == "1":
                ret = 1
            elif m.group('noresult') == "1":
                ret = 2
            line = line.strip('\n')
            bb.note(line)
        else:
            line = line.strip('\n')
            bb.note(line)
    f.close()

    if ret != 0:
        raise bb.build.FuncFailed("Some testcases fail, pls. check test result and test log!!!")

