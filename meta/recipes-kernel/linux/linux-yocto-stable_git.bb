inherit kernel
require linux-yocto.inc

KMACHINE_qemux86  = "common_pc"
KMACHINE_qemux86-64  = "common_pc_64"
KMACHINE_qemuppc  = "qemu_ppc32"
KMACHINE_qemumips = "mti_malta32_be"
KMACHINE_qemuarm  = "arm_versatile_926ejs"
KMACHINE_atom-pc  = "atom-pc"
KMACHINE_routerstationpro = "routerstationpro"
KMACHINE_mpc8315e-rdb = "fsl-mpc8315e-rdb"
KMACHINE_beagleboard = "beagleboard"

LINUX_VERSION ?= "2.6.34"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE_EXTENSION}"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64|atom-pc|routerstationpro|mpc8315e-rdb|beagleboard)"

# this performs a fixup on the SRCREV for new/undefined BSPs
python __anonymous () {
    import bb, re, string

    rev = bb.data.getVar("SRCREV_machine", d, 1)
    if rev == "standard":
        bb.data.setVar("SRCREV_machine", "${SRCREV_meta}", d)

    kerntype = string.replace(bb.data.expand("${LINUX_KERNEL_TYPE}", d), "_", "-")
    bb.data.setVar("LINUX_KERNEL_TYPE_EXTENSION", kerntype, d)
}

SRC_URI = "git://git.pokylinux.org/linux-2.6-windriver.git;protocol=git;fullclone=1;branch=${KBRANCH};name=machine \
           git://git.pokylinux.org/linux-2.6-windriver.git;protocol=git;noclone=1;branch=wrs_meta;name=meta"


# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES=features/netfilter

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout

require linux-tools.inc
