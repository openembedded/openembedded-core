KBRANCH ?= "v5.10/standard/base"

require recipes-kernel/linux/linux-yocto.inc
include cve-exclusion_5.10.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.10/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.10/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.10/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.10/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.10/standard/base"
KBRANCH:qemuriscv32  ?= "v5.10/standard/base"
KBRANCH:qemux86  ?= "v5.10/standard/base"
KBRANCH:qemux86-64 ?= "v5.10/standard/base"
KBRANCH:qemumips64 ?= "v5.10/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "e8c5b2165b5bfcef1358cd6fa70368fc174ac2f4"
SRCREV_machine:qemuarm64 ?= "d8165863c5d9467c555ef97318193595242b85a7"
SRCREV_machine:qemumips ?= "54bad1b3d7ed3c3cf1ccb98ee21ae2ed059c20b0"
SRCREV_machine:qemuppc ?= "7ab0ce8ef9f6f1b4c12f3594b1f410f84ef35bfa"
SRCREV_machine:qemuriscv64 ?= "c723f1548ae98a23895a85e67466df935d09e594"
SRCREV_machine:qemuriscv32 ?= "c723f1548ae98a23895a85e67466df935d09e594"
SRCREV_machine:qemux86 ?= "c723f1548ae98a23895a85e67466df935d09e594"
SRCREV_machine:qemux86-64 ?= "c723f1548ae98a23895a85e67466df935d09e594"
SRCREV_machine:qemumips64 ?= "702fe224e3553677ba09d28e45b7eaee31c667f4"
SRCREV_machine ?= "c723f1548ae98a23895a85e67466df935d09e594"
SRCREV_meta ?= "f16f63fae82fd0565f9314a61ae6be1d0509eb24"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.191"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
