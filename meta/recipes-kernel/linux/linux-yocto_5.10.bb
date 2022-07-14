KBRANCH ?= "v5.10/standard/base"

require recipes-kernel/linux/linux-yocto.inc

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

SRCREV_machine:qemuarm ?= "859babe91947ce09d70f8be4c8b314d9712a30ea"
SRCREV_machine:qemuarm64 ?= "0a59ce2115693d52b931b0fc72f5699a22a77e81"
SRCREV_machine:qemumips ?= "5bebdf68c5391f8a56f98a8cb31522c0627e05e0"
SRCREV_machine:qemuppc ?= "0f4a4f1329c7331a02d36f7195f9414e40709d43"
SRCREV_machine:qemuriscv64 ?= "a6e419710dedbe0390cbf30267c13752b9161972"
SRCREV_machine:qemuriscv32 ?= "a6e419710dedbe0390cbf30267c13752b9161972"
SRCREV_machine:qemux86 ?= "a6e419710dedbe0390cbf30267c13752b9161972"
SRCREV_machine:qemux86-64 ?= "a6e419710dedbe0390cbf30267c13752b9161972"
SRCREV_machine:qemumips64 ?= "03dc9bc06ba1b7d105467bea334e84b3666b67f4"
SRCREV_machine ?= "a6e419710dedbe0390cbf30267c13752b9161972"
SRCREV_meta ?= "96ea2660bb97e15f48f4885b9e436f24c3606bd9"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.130"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

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
