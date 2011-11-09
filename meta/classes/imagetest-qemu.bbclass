# Test related variables
# By default, TEST_DIR is created under WORKDIR
TEST_DIR ?= "${WORKDIR}/qemuimagetest"
TEST_LOG ?= "${LOG_DIR}/qemuimagetests"
TEST_RESULT ?= "${TEST_DIR}/result"
TEST_TMP ?= "${TEST_DIR}/tmp"
TEST_SCEN ?= "sanity"
TEST_STATUS ?= "${TEST_TMP}/status"
TARGET_IPSAVE ?= "${TEST_TMP}/target_ip"
TEST_SERIALIZE ?= "1"

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
    import shutil
    
    """
    Test Controller for automated testing.
    """
    
    casestr = re.compile(r'(?P<scen>\w+\b):(?P<case>\S+$)')
    resultstr = re.compile(r'\s*(?P<case>\w+)\s*(?P<pass>\d+)\s*(?P<fail>\d+)\s*(?P<noresult>\d+)')
    machine = d.getVar('MACHINE', 1)
    pname = d.getVar('PN', 1)
    
    """function to save test cases running status"""
    def teststatus(test, status, index, length):
        test_status = d.getVar('TEST_STATUS', 1)
        if not os.path.exists(test_status):
            raise bb.build.FuncFailed("No test status file existing under TEST_TMP")

        f = open(test_status, "w")
        f.write("\t%-15s%-15s%-15s%-15s\n" % ("Case", "Status", "Number", "Total"))
        f.write("\t%-15s%-15s%-15s%-15s\n" % (case, status, index, length))
        f.close()

    """funtion to run each case under scenario"""
    def runtest(scen, case, fulltestpath):
        resultpath = d.getVar('TEST_RESULT', 1)
        tmppath = d.getVar('TEST_TMP', 1)

        """initialize log file for testcase"""
        logpath = d.getVar('TEST_LOG', 1)
        bb.utils.mkdirhier("%s/%s" % (logpath, scen))
        caselog = os.path.join(logpath, "%s/log_%s.%s" % (scen, case, d.getVar('DATETIME', 1)))
        os.system("touch %s" % caselog)
        
        """export TEST_TMP, TEST_RESULT, DEPLOY_DIR and QEMUARCH"""
        os.environ["PATH"] = d.getVar("PATH", True)
        os.environ["TEST_TMP"] = tmppath
        os.environ["TEST_RESULT"] = resultpath
        os.environ["DEPLOY_DIR"] = d.getVar("DEPLOY_DIR", True)
        os.environ["QEMUARCH"] = machine
        os.environ["QEMUTARGET"] = pname
        os.environ["DISPLAY"] = d.getVar("DISPLAY", True)
        os.environ["COREBASE"] = d.getVar("COREBASE", True)
        os.environ["TOPDIR"] = d.getVar("TOPDIR", True)
        os.environ["OE_TMPDIR"] = d.getVar("TMPDIR", True)
        os.environ["TEST_STATUS"] = d.getVar("TEST_STATUS", True)
        os.environ["TARGET_IPSAVE"] = d.getVar("TARGET_IPSAVE", True)
        os.environ["TEST_SERIALIZE"] = d.getVar("TEST_SERIALIZE", True)
        os.environ["SDK_NAME"] = d.getVar("SDK_NAME", True)

        """run Test Case"""
        bb.note("Run %s test in scenario %s" % (case, scen))
        os.system("%s" % fulltestpath)
    
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
                for dir in d.getVar("QEMUIMAGETESTS", True).split():
                    fulltestcase = os.path.join(dir, item, casefile)
                    if not os.path.isfile(fulltestcase):
                        raise bb.build.FuncFailed("Testcase %s not found" % fulltestcase)
                    list.append((item, casefile, fulltestcase))
            else:
                for dir in d.getVar("QEMUIMAGETESTS", True).split():
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

    """Clean tmp folder for testing"""
    def clean_tmp():
        tmppath = d.getVar('TEST_TMP', 1)

        if os.path.isdir(tmppath):
            for f in os.listdir(tmppath):
                tmpfile = os.path.join(tmppath, f)
		if os.path.isfile(tmpfile):
                    os.remove(tmpfile)
                elif os.path.isdir(tmpfile):
                    shutil.rmtree(tmpfile, True)

    """Before running testing, clean temp folder first"""
    clean_tmp()

    """check testcase folder and create test log folder"""
    testpath = d.getVar('TEST_DIR', 1)
    bb.utils.mkdirhier(testpath)
    
    logpath = d.getVar('TEST_LOG', 1)
    bb.utils.mkdirhier(logpath)

    tmppath = d.getVar('TEST_TMP', 1)
    bb.utils.mkdirhier(tmppath)

    """initialize test status file"""
    test_status = d.getVar('TEST_STATUS', 1)
    if os.path.exists(test_status):
        os.remove(test_status)
    os.system("touch %s" % test_status)

    """initialize result file"""
    resultpath = d.getVar('TEST_RESULT', 1)
    bb.utils.mkdirhier(resultpath)
    resultfile = os.path.join(resultpath, "testresult.%s" % d.getVar('DATETIME', 1))
    sresultfile = os.path.join(resultpath, "testresult.log")

    machine = d.getVar('MACHINE', 1)

    if os.path.exists(sresultfile):
        os.remove(sresultfile)
    os.system("touch %s" % resultfile)
    os.symlink(resultfile, sresultfile)
    f = open(sresultfile, "a")
    f.write("\tTest Result for %s %s\n" % (machine, pname))
    f.write("\t%-15s%-15s%-15s%-15s\n" % ("Testcase", "PASS", "FAIL", "NORESULT"))
    f.close()
    
    """generate pre-defined testcase list"""
    testlist = d.getVar('TEST_SCEN', 1)
    fulllist = generate_list(testlist)

    """Begin testing"""
    for index,test in enumerate(fulllist):
        (scen, case, fullpath) = test
        teststatus(case, "running", index, (len(fulllist) - 1))
        runtest(scen, case, fullpath)
        teststatus(case, "finished", index, (len(fulllist) - 1))
    
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

    """Clean temp files for testing"""
    clean_tmp()

    if ret != 0:
        raise bb.build.FuncFailed("Some testcases fail, pls. check test result and test log!!!")

