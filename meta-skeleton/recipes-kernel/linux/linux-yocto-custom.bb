# linux-yocto-custom.bb:
#
#   Provides an example/minimal kernel recipe that uses the linux-yocto
#   and oe-core kernel classes to apply a subset of yocto kernel 
#   management to git managed kernel repositories.
#
# Notes:
#
#   kconfig(s): the kernel must be configured with a defconfig, or via
#               configuration fragment(s). Either of these can be added
#               via bbappend.
#   patches: patches can be merged into to the source git tree itself, added
#            using standard bbappend syntax or controlled via .scc feature 
#            descriptions (also via bbappends)
#   
#   example configuration addition:
#            SRC_URI += "file://smp.cfg"
#   example patch addition (for kernel v3.4 only):
#            SRC_URI += "file://0001-linux-version-tweak.patch
#   example feature addition (for kernel v3.4 only):
#            SRC_URI += "file://feature.scc"
#
# Warning:
#
#   Building the sample kernel tree (kernel.org) without providing any
#   configuration will result in build or boot errors. This is not a bug
#   it is a required element for creating a valid kernel.
#    

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=git;nocheckout=1"

LINUX_VERSION ?= "3.4"
LINUX_VERSION_EXTENSION ?= "-custom"

# tag: v3.4 76e10d158efb6d4516018846f60c2ab5501900bc
SRCREV="76e10d158efb6d4516018846f60c2ab5501900bc"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"
