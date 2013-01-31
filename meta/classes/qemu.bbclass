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

    dynamic_loader = rootfs_path + '$(readelf -l ' + rootfs_path + \
                     binary + '| grep "Requesting program interpreter"|sed -e \'s/^.*\[.*: \(.*\)\]/\\1/\')'
    library_path = rootfs_path + data.getVar("base_libdir", True) + ":" + \
                   rootfs_path + data.getVar("libdir", True)

    return qemu_binary + " " + dynamic_loader + " --library-path " + library_path \
           + " " + rootfs_path + binary
