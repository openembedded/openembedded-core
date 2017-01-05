# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)


# testimage.bbclass enables testing of qemu images using python unittests.
# Most of the tests are commands run on target image over ssh.
# To use it add testimage to global inherit and call your target image with -c testimage
# You can try it out like this:
# - first build a qemu core-image-sato
# - add IMAGE_CLASSES += "testimage" in local.conf
# - then bitbake core-image-sato -c testimage. That will run a standard suite of tests.

# You can set (or append to) TEST_SUITES in local.conf to select the tests
# which you want to run for your target.
# The test names are the module names in meta/lib/oeqa/runtime.
# Each name in TEST_SUITES represents a required test for the image. (no skipping allowed)
# Appending "auto" means that it will try to run all tests that are suitable for the image (each test decides that on it's own).
# Note that order in TEST_SUITES is relevant: tests are run in an order such that
# tests mentioned in @skipUnlessPassed run before the tests that depend on them,
# but without such dependencies, tests run in the order in which they are listed
# in TEST_SUITES.
#
# A layer can add its own tests in lib/oeqa/runtime, provided it extends BBPATH as normal in its layer.conf.

# TEST_LOG_DIR contains a command ssh log and may contain infromation about what command is running, output and return codes and for qemu a boot log till login.
# Booting is handled by this class, and it's not a test in itself.
# TEST_QEMUBOOT_TIMEOUT can be used to set the maximum time in seconds the launch code will wait for the login prompt.

TEST_LOG_DIR ?= "${WORKDIR}/testimage"

TEST_EXPORT_DIR ?= "${TMPDIR}/testimage/${PN}"
TEST_INSTALL_TMP_DIR ?= "${WORKDIR}/testimage/install_tmp"
TEST_NEEDED_PACKAGES_DIR ?= "${WORKDIR}/testimage/packages"
TEST_EXTRACTED_DIR ?= "${TEST_NEEDED_PACKAGES_DIR}/extracted"
TEST_PACKAGED_DIR ?= "${TEST_NEEDED_PACKAGES_DIR}/packaged"

RPMTESTSUITE = "${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'smart rpm', '', d)}"
MINTESTSUITE = "ping"
NETTESTSUITE = "${MINTESTSUITE} ssh df date scp syslog"
DEVTESTSUITE = "gcc kernelmodule ldd"

DEFAULT_TEST_SUITES = "${MINTESTSUITE} auto"
DEFAULT_TEST_SUITES_pn-core-image-minimal = "${MINTESTSUITE}"
DEFAULT_TEST_SUITES_pn-core-image-minimal-dev = "${MINTESTSUITE}"
DEFAULT_TEST_SUITES_pn-core-image-full-cmdline = "${NETTESTSUITE} perl python logrotate"
DEFAULT_TEST_SUITES_pn-core-image-x11 = "${MINTESTSUITE}"
DEFAULT_TEST_SUITES_pn-core-image-lsb = "${NETTESTSUITE} pam parselogs ${RPMTESTSUITE}"
DEFAULT_TEST_SUITES_pn-core-image-sato = "${NETTESTSUITE} connman xorg parselogs ${RPMTESTSUITE} \
    ${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'python', '', d)}"
DEFAULT_TEST_SUITES_pn-core-image-sato-sdk = "${NETTESTSUITE} connman xorg perl python \
    ${DEVTESTSUITE} parselogs ${RPMTESTSUITE}"
DEFAULT_TEST_SUITES_pn-core-image-lsb-dev = "${NETTESTSUITE} pam perl python parselogs ${RPMTESTSUITE}"
DEFAULT_TEST_SUITES_pn-core-image-lsb-sdk = "${NETTESTSUITE} buildcvs buildiptables buildgalculator \
    connman ${DEVTESTSUITE} pam perl python parselogs ${RPMTESTSUITE}"
DEFAULT_TEST_SUITES_pn-meta-toolchain = "auto"

# aarch64 has no graphics
DEFAULT_TEST_SUITES_remove_aarch64 = "xorg"

# qemumips is quite slow and has reached the timeout limit several times on the YP build cluster,
# mitigate this by removing build tests for qemumips machines.
MIPSREMOVE ??= "buildcvs buildiptables buildgalculator"
DEFAULT_TEST_SUITES_remove_qemumips = "${MIPSREMOVE}"
DEFAULT_TEST_SUITES_remove_qemumips64 = "${MIPSREMOVE}"

TEST_SUITES ?= "${DEFAULT_TEST_SUITES}"

TEST_QEMUBOOT_TIMEOUT ?= "1000"
TEST_TARGET ?= "qemu"

TESTIMAGEDEPENDS = ""
TESTIMAGEDEPENDS_qemuall = "qemu-native:do_populate_sysroot qemu-helper-native:do_populate_sysroot"
TESTIMAGEDEPENDS += "${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'cpio-native:do_populate_sysroot', '', d)}"
TESTIMAGEDEPENDS_qemuall += "${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'cpio-native:do_populate_sysroot', '', d)}"
TESTIMAGEDEPENDS_qemuall += "${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'createrepo-native:do_populate_sysroot', '', d)}"
TESTIMAGEDEPENDS += "${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'python-smartpm-native:do_populate_sysroot', '', d)}"
TESTIMAGEDEPENDS += "${@bb.utils.contains('IMAGE_PKGTYPE', 'ipk', 'opkg-utils-native:do_populate_sysroot', '', d)}"
TESTIMAGEDEPENDS += "${@bb.utils.contains('IMAGE_PKGTYPE', 'deb', 'apt-native:do_populate_sysroot', '', d)}"
TESTIMAGEDEPENDS += "${@bb.utils.contains('IMAGE_PKGTYPE', 'rpm', 'python-smartpm-native:do_populate_sysroot', '', d)}"


TESTIMAGELOCK = "${TMPDIR}/testimage.lock"
TESTIMAGELOCK_qemuall = ""

TESTIMAGE_DUMP_DIR ?= "/tmp/oe-saved-tests/"

testimage_dump_target () {
    top -bn1
    ps
    free
    df
    # The next command will export the default gateway IP
    export DEFAULT_GATEWAY=$(ip route | awk '/default/ { print $3}')
    ping -c3 $DEFAULT_GATEWAY
    dmesg
    netstat -an
    ip address
    # Next command will dump logs from /var/log/
    find /var/log/ -type f 2>/dev/null -exec echo "====================" \; -exec echo {} \; -exec echo "====================" \; -exec cat {} \; -exec echo "" \;
}

testimage_dump_host () {
    top -bn1
    iostat -x -z -N -d -p ALL 20 2
    ps -ef
    free
    df
    memstat
    dmesg
    ip -s link
    netstat -an
}

python do_testimage() {

    testimage_sanity(d)

    testimage_main(d)
}

addtask testimage
do_testimage[nostamp] = "1"
do_testimage[depends] += "${TESTIMAGEDEPENDS}"
do_testimage[lockfiles] += "${TESTIMAGELOCK}"

def testimage_sanity(d):
    if (d.getVar('TEST_TARGET') == 'simpleremote'
        and (not d.getVar('TEST_TARGET_IP')
             or not d.getVar('TEST_SERVER_IP'))):
        bb.fatal('When TEST_TARGET is set to "simpleremote" '
                 'TEST_TARGET_IP and TEST_SERVER_IP are needed too.')

def testimage_main(d):
    import os
    import signal
    import json
    import sys
    import logging
    import time

    from bb.utils import export_proxies
    from oeqa.runtime.context import OERuntimeTestContext
    from oeqa.runtime.context import OERuntimeTestContextExecutor
    from oeqa.core.target.qemu import supported_fstypes
    from oeqa.utils import make_logger_bitbake_compatible

    def sigterm_exception(signum, stackframe):
        """
        Catch SIGTERM from worker in order to stop qemu.
        """
        raise RuntimeError

    logger = make_logger_bitbake_compatible(logging.getLogger("BitBake"))
    pn = d.getVar("PN")

    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR"))
    #test_create_extract_dirs(d)

    image_name = ("%s/%s" % (d.getVar('DEPLOY_DIR_IMAGE'),
                             d.getVar('IMAGE_LINK_NAME')))

    tdname = "%s.testdata.json" % image_name
    td = json.load(open(tdname, "r"))

    image_manifest = "%s.manifest" % image_name
    image_packages = OERuntimeTestContextExecutor.readPackagesManifest(image_manifest)

    # Get machine
    machine = d.getVar("MACHINE")

    # Get rootfs
    fstypes = [fs for fs in d.getVar('IMAGE_FSTYPES').split(' ')
                  if fs in supported_fstypes]
    if not fstypes:
        bb.fatal('Unsupported image type built. Add a comptible image to '
                 'IMAGE_FSTYPES. Supported types: %s' %
                 ', '.join(supported_fstypes))
    rootfs = '%s.%s' % (image_name, fstypes[0])

    # Get tmpdir (not really used, just for compatibility)
    tmpdir = d.getVar("TMPDIR")

    # Get deploy_dir_image (not really used, just for compatibility)
    dir_image = d.getVar("DEPLOY_DIR_IMAGE")

    # Get bootlog
    bootlog = os.path.join(d.getVar("TEST_LOG_DIR"),
                           'qemu_boot_log.%s' % d.getVar('DATETIME'))

    # Get display
    display = d.getVar("BB_ORIGENV").getVar("DISPLAY")

    # Get kernel
    kernel_name = ('%s-%s.bin' % (d.getVar("KERNEL_IMAGETYPE"), machine))
    kernel = os.path.join(d.getVar("DEPLOY_DIR_IMAGE"), kernel_name)

    # Get boottime
    boottime = int(d.getVar("TEST_QEMUBOOT_TIMEOUT"))

    # Get use_kvm
    qemu_use_kvm = d.getVar("QEMU_USE_KVM")
    if qemu_use_kvm and qemu_use_kvm == 'True' and 'x86' in machine:
        kvm = True
    else:
        kvm = False

    # TODO: We use the current implementatin of qemu runner because of
    # time constrains, qemu runner really needs a refactor too.
    target_kwargs = { 'machine'     : machine,
                      'rootfs'      : rootfs,
                      'tmpdir'      : tmpdir,
                      'dir_image'   : dir_image,
                      'display'     : display,
                      'kernel'      : kernel,
                      'boottime'    : boottime,
                      'bootlog'     : bootlog,
                      'kvm'         : kvm,
                    }

    # runtime use network for download projects for build
    export_proxies(d)

    # we need the host dumper in test context
    host_dumper = OERuntimeTestContextExecutor.getHostDumper(
        d.getVar("testimage_dump_host"),
        d.getVar("TESTIMAGE_DUMP_DIR"))

    # the robot dance
    target = OERuntimeTestContextExecutor.getTarget(
        d.getVar("TEST_TARGET"), None, d.getVar("TEST_TARGET_IP"),
        d.getVar("TEST_SERVER_IP"), **target_kwargs)

    # test context
    tc = OERuntimeTestContext(td, logger, target, host_dumper, image_packages)

    # Load tests before starting the target
    test_paths = get_runtime_paths(d)
    test_modules = d.getVar('TEST_SUITES')
    tc.loadTests(test_paths, modules=test_modules)

    bootparams = None
    if d.getVar('VIRTUAL-RUNTIME_init_manager', '') == 'systemd':
        bootparams = 'systemd.log_level=debug systemd.log_target=console'

    results = None
    orig_sigterm_handler = signal.signal(signal.SIGTERM, sigterm_exception)
    try:
        # We need to check if runqemu ends unexpectedly
        # or if the worker send us a SIGTERM
        tc.target.start(extra_bootparams=bootparams)
        results = tc.runTests()
    except (RuntimeError, BlockingIOError) as err:
        if isinstance(err, RuntimeError):
            bb.error('testimage received SIGTERM, shutting down...')
        else:
            bb.error('runqemu failed, shutting down...')
        if results:
            results.stop()
            results = None
    finally:
        signal.signal(signal.SIGTERM, orig_sigterm_handler)
        tc.target.stop()

    # Show results (if we have them)
    if not results:
        bb.fatal('%s - FAILED - tests were interrupted during execution' % pn)
    tc.logSummary(results, pn)
    tc.logDetails()
    if not results.wasSuccessful():
        bb.fatal('%s - FAILED - check the task log and the ssh log' % pn)

def get_runtime_paths(d):
    """
    Returns a list of paths where runtime test must reside.

    Runtime tests are expected in <LAYER_DIR>/lib/oeqa/runtime/cases/
    """
    paths = []

    for layer in d.getVar('BBLAYERS').split():
        path = os.path.join(layer, 'lib/oeqa/runtime/cases')
        if os.path.isdir(path):
            paths.append(path)
    return paths

def test_create_extract_dirs(d):
    install_path = d.getVar("TEST_INSTALL_TMP_DIR")
    package_path = d.getVar("TEST_PACKAGED_DIR")
    extracted_path = d.getVar("TEST_EXTRACTED_DIR")
    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR"))
    bb.utils.remove(package_path, recurse=True)
    bb.utils.mkdirhier(install_path)
    bb.utils.mkdirhier(package_path)
    bb.utils.mkdirhier(extracted_path)


testimage_main[vardepsexclude] += "BB_ORIGENV DATETIME"

inherit testsdk
