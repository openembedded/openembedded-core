inherit kernel
require linux-yocto.inc

KMACHINE = "common_pc"
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

KMETA = wrs_meta
KBRANCH = ${KMACHINE}-${LINUX_KERNEL_TYPE_EXTENSION}

SRCREV_machine_qemuarm = "a2fb081b44d6368eff8d28e2fdb991c61b1f428e"
SRCREV_machine_qemumips = "c32d40f960e3c89d07f079bec4c96dcbfc749f0b"
SRCREV_machine_qemuppc = "96d6bc31d3caaf62a966255479cc5cee0e76b1e9"
SRCREV_machine_qemux86 = "72ca49ab08b8eb475cec82a10049503602325791"
SRCREV_machine_qemux86-64 = "72ca49ab08b8eb475cec82a10049503602325791"
SRCREV_machine_atom-pc = "72ca49ab08b8eb475cec82a10049503602325791"
SRCREV_machine_routerstationpro = "49745cd45c92a89e70c6e2334caa80818c134562"
SRCREV_machine_mpc8315e-rdb = "a1c0ed6bf4060c10874b2a8547d81b3169dcf16a"
SRCREV_machine_beagleboard = "ef7f944e773950d4016b7643f9ecf052bbe250cd"
SRCREV_machine = "72ca49ab08b8eb475cec82a10049503602325791"
SRCREV_meta = "ec26387cb168e9e0976999b528b5a9dd62e3157a"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64|atom-pc|routerstationpro|mpc8315e-rdb|beagleboard)"

# this performs a fixup on historical kernel types with embedded _'s
python __anonymous () {
    import bb, re, string

    kerntype = string.replace(bb.data.expand("${LINUX_KERNEL_TYPE}", d), "_", "-")
    bb.data.setVar("LINUX_KERNEL_TYPE_EXTENSION", kerntype, d)
}

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.34.git;protocol=git;nocheckout=1;branch=${KBRANCH},wrs_meta;name=machine,meta"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES=features/netfilter

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout

require linux-tools.inc
