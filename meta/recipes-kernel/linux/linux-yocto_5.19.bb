KBRANCH ?= "v5.19/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.19/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.19/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.19/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.19/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.19/standard/base"
KBRANCH:qemuriscv32  ?= "v5.19/standard/base"
KBRANCH:qemux86  ?= "v5.19/standard/base"
KBRANCH:qemux86-64 ?= "v5.19/standard/base"
KBRANCH:qemumips64 ?= "v5.19/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "4a8660a840a9f9beede5a5a45dc48031b4416ff0"
SRCREV_machine:qemuarm64 ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_machine:qemumips ?= "6f3e66bc642900e59b6d831c0b8a0cf3aa1a62ce"
SRCREV_machine:qemuppc ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_machine:qemuriscv64 ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_machine:qemuriscv32 ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_machine:qemux86 ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_machine:qemux86-64 ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_machine:qemumips64 ?= "544923be2a8b7b540209f35fa0b6e893ac39a599"
SRCREV_machine ?= "dcad83c4e07ac89ae873778ace4efb06dbf4898f"
SRCREV_meta ?= "db8b42f2f808023c75cb41c89e866fe526ce6876"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "d235c2b1f470f012bda26844aabf26321b1c446a"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.19/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.19;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.19.16"

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
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

