KBRANCH ?= "v5.15/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.15/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.15/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.15/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.15/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.15/standard/base"
KBRANCH:qemuriscv32  ?= "v5.15/standard/base"
KBRANCH:qemux86  ?= "v5.15/standard/base"
KBRANCH:qemux86-64 ?= "v5.15/standard/base"
KBRANCH:qemumips64 ?= "v5.15/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "af65cb567e671fb5eea7224f9252a9cdfd37f7d0"
SRCREV_machine:qemuarm64 ?= "6101a726c5cfd4c995f9ed3cec3dadc593f1765b"
SRCREV_machine:qemumips ?= "24b1491fde2da59de37bf4b43e49e4d9725a700e"
SRCREV_machine:qemuppc ?= "09492ed54dade6f3e646b31be5e70b07b0c22755"
SRCREV_machine:qemuriscv64 ?= "5bd4bda819e9c9736b3ab14e9295b8166c61b6a4"
SRCREV_machine:qemuriscv32 ?= "5bd4bda819e9c9736b3ab14e9295b8166c61b6a4"
SRCREV_machine:qemux86 ?= "5bd4bda819e9c9736b3ab14e9295b8166c61b6a4"
SRCREV_machine:qemux86-64 ?= "5bd4bda819e9c9736b3ab14e9295b8166c61b6a4"
SRCREV_machine:qemumips64 ?= "98e983921ddecb99fe11439c033273b90cc5d413"
SRCREV_machine ?= "5bd4bda819e9c9736b3ab14e9295b8166c61b6a4"
SRCREV_meta ?= "ea948a0983d7b7820814e5bce4eda3079201bd95"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE:class-devupstream = "-1"
SRCREV_machine:class-devupstream ?= "8993e6067f263765fd26edabf3e3012e3ec4d81e"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.15/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.15.26"

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
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

