addtask qemuimagetest before do_build
# after do_rootfs
do_qemuimagetest[nostamp] = "1"
do_qemuimagetest[depends] += "qemu-native:do_populate_sysroot"

# Test related variables
# By default, TEST_DIR is created under WORKDIR
TEST_DIR ?= "${WORKDIR}/qemuimagetest"
TEST_LOG ?= "${LOG_DIR}/qemuimagetests"
TEST_RESULT ?= "${TEST_DIR}/result"
TEST_LIST ?= "${TEST_DIR}/list"
TEST_TMP ?= "${TEST_DIR}/tmp"
TEST_SCEN ?= "sanity"

python do_qemuimagetest() {
    import sys
    import re
    import os
    
    """
    Test Controller for Poky Testing.
    """
    
    casestr = re.compile(r'(?P<scen>\w+\b)\s*(?P<case>\w+$)')
    resultstr = re.compile(r'\s*(?P<case>\w+)\s*(?P<pass>\d+)\s*(?P<fail>\d+)\s*(?P<noresult>\d+)')
    
    """funtion to run each case under scenario"""
    def runtest(scen, case, fulltestpath):
        resultpath = bb.data.getVar('TEST_RESULT', d, 1)
        machine = bb.data.getVar('MACHINE', d, 1)
        pname = bb.data.getVar('PN', d, 1)

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

        """run Test Case"""
        bb.note("Run %s test in scenario %s" % (case, scen))
        os.system("%s | tee -a %s" % (fulltestpath, caselog))
    
    """Generate testcase list in runtime"""
    def generate_list(testfile, testlist):
        list = []
        if len(testlist) == 0:
            raise bb.build.FuncFailed("No testcase defined in TEST_SCEN")

        """remove old testcase list file"""
        if os.path.exists(testfile):
            os.remove(testfile)
        os.system("touch %s" % testfile)

        """check testcase folder and add case to TEST_LIST"""
        for item in testlist.split(" "):
            found = False
            for dir in bb.data.getVar("QEMUIMAGETESTS", d, True).split():
                casepath = os.path.join(dir, item)
                if not os.path.isdir(casepath):
                    continue
                files = os.listdir(casepath)
                for casefile in files:
                    fulltestcase = "%s/%s" % (casepath, casefile)
                    if os.path.isfile(fulltestcase):
                        list.append((item, casefile, fulltestcase))
                    found = True
            if not found:
                raise bb.build.FuncFailed("Testcase folder not found for test %s" % item)
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
    f.write("\tTestcase\tPASS\tFAIL\tNORESULT\n")
    f.close()
    
    """generate pre-defined testcase list"""
    testfile = bb.data.getVar('TEST_LIST', d, 1)
    testlist = bb.data.getVar('TEST_SCEN', d, 1)
    fulllist = generate_list(testfile, testlist)

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
            print line,
        else:
            print line,
    f.close()

    if ret != 0:
        raise bb.build.FuncFailed("Some testcases fail, pls. check test result and test log!!!")
}
