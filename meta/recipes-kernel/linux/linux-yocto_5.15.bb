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

SRCREV_machine:qemuarm ?= "f6561f66ec8485b0f55fa5c09e3614d85e65da77"
SRCREV_machine:qemuarm64 ?= "182d2a0dfea7f5a869b8938b5c50fd46153047ff"
SRCREV_machine:qemumips ?= "5954e50bf90b3e22361b335b3fab47fcbd14a88e"
SRCREV_machine:qemuppc ?= "114761d623636cafa01bb60b3e0e9262f21d2d56"
SRCREV_machine:qemuriscv64 ?= "ee43e792dbdecc18d6d72c360712659fc78b997f"
SRCREV_machine:qemuriscv32 ?= "ee43e792dbdecc18d6d72c360712659fc78b997f"
SRCREV_machine:qemux86 ?= "ee43e792dbdecc18d6d72c360712659fc78b997f"
SRCREV_machine:qemux86-64 ?= "ee43e792dbdecc18d6d72c360712659fc78b997f"
SRCREV_machine:qemumips64 ?= "f57326efb3ee9cba3f867b65b7535d3819da7933"
SRCREV_machine ?= "ee43e792dbdecc18d6d72c360712659fc78b997f"
SRCREV_meta ?= "7dd85143b4ed77bdcb6628ac8ea69917e1cd29e5"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "e29be6724adbc9c3126d2a9550ec21f927f22f6d"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.15/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.15.32"

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

