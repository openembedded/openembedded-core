#
# This class contains functions for recipes that need QEMU or test for its
# existence.
#

def qemu_target_binary(data):
    target_arch = data.getVar("TARGET_ARCH", True)
    if target_arch in ("i486", "i586", "i686"):
        target_arch = "i386"
    elif target_arch == "powerpc":
        target_arch = "ppc"
    elif target_arch == "powerpc64":
        target_arch = "ppc64"

    return "qemu-" + target_arch
#
# Next function will return a string containing the command that is needed to
# to run a certain binary through qemu. For example, in order to make a certain
# postinstall scriptlet run at do_rootfs time and running the postinstall is
# architecture dependent, we can run it through qemu. For example, in the
# postinstall scriptlet, we could use the following:
#
# ${@qemu_run_binary(d, '$D', '/usr/bin/test_app')} [test_app arguments]
#
def qemu_run_binary(data, rootfs_path, binary):
    qemu_binary = qemu_target_binary(data)
    if qemu_binary == "qemu-allarch":
        qemu_binary = "qemuwrapper"

    libdir = rootfs_path + data.getVar("libdir", False)
    base_libdir = rootfs_path + data.getVar("base_libdir", False)

    return "PSEUDO_UNLOAD=1 " + qemu_binary + " -L " + rootfs_path\
            + " -E LD_LIBRARY_PATH=" + libdir + ":" + base_libdir + " "\
            + rootfs_path + binary

QEMU_OPTIONS = ""
QEMU_OPTIONS_iwmmxt    = "-cpu pxa270-c5"
QEMU_OPTIONS_armv6     = "-cpu arm1136"
QEMU_OPTIONS_armv7a    = "-cpu cortex-a8"
QEMU_OPTIONS_e500v2    = "-cpu e500v2"
QEMU_OPTIONS_e500mc    = "-cpu e500mc"
QEMU_OPTIONS_e5500     = "-cpu e5500"
QEMU_OPTIONS_e5500-64b = "-cpu e5500"
QEMU_OPTIONS_e6500     = "-cpu e6500"
QEMU_OPTIONS_e6500-64b = "-cpu e6500"
QEMU_OPTIONS_ppc7400   = "-cpu 7400"
