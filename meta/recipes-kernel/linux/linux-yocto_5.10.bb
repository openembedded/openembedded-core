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

SRCREV_machine:qemuarm ?= "6d1f4e2827227663603efe419dc601860608862b"
SRCREV_machine:qemuarm64 ?= "8edb39e7f3399ef6f97a416069777d0230ad68bc"
SRCREV_machine:qemumips ?= "c37227bf802d3c489639a031f1cd8ee9e51ba044"
SRCREV_machine:qemuppc ?= "f00b3a49bc6e9ca12497cf50080c391e59696d06"
SRCREV_machine:qemuriscv64 ?= "11b247211e00c610587b27a762e4814f56723958"
SRCREV_machine:qemuriscv32 ?= "11b247211e00c610587b27a762e4814f56723958"
SRCREV_machine:qemux86 ?= "11b247211e00c610587b27a762e4814f56723958"
SRCREV_machine:qemux86-64 ?= "11b247211e00c610587b27a762e4814f56723958"
SRCREV_machine:qemumips64 ?= "0c17abc2a99b68c19099df5b61653729f6d17f76"
SRCREV_machine ?= "11b247211e00c610587b27a762e4814f56723958"
SRCREV_meta ?= "d660385f970bb5c61304176c107a4958db9304e8"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.192"

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
