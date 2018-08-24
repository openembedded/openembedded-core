KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "fcfdd4f0304cfddb3703ddbb38099a424102d5ed"
SRCREV_machine_qemuarm64 ?= "ff1b393ffe88c42d06185ae3ffbbc76856516631"
SRCREV_machine_qemumips ?= "f7a7823cf33e772e9fd73239e57bac2b6a57cd87"
SRCREV_machine_qemuppc ?= "97d3f96ea2ed5efda4663fd0f0c4e0c21e43c863"
SRCREV_machine_qemux86 ?= "1839f1b10cf9895ac0e31631eaa9a5ed5c63ede0"
SRCREV_machine_qemux86-64 ?= "1839f1b10cf9895ac0e31631eaa9a5ed5c63ede0"
SRCREV_machine_qemumips64 ?= "db539ae62da0b5fe7d065830c2709abc01a5591d"
SRCREV_machine ?= "1839f1b10cf9895ac0e31631eaa9a5ed5c63ede0"
SRCREV_meta ?= "f97272690ca7e1143b9db3c1f1a01d9c37b6f478"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.62"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
