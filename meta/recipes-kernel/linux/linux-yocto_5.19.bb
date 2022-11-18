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

SRCREV_machine:qemuarm ?= "d789efca1869e21f1c275fd2e46f2eada1698f8d"
SRCREV_machine:qemuarm64 ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_machine:qemumips ?= "7216e7029faf5685256b3ce57d3be1c371c56267"
SRCREV_machine:qemuppc ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_machine:qemuriscv64 ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_machine:qemuriscv32 ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_machine:qemux86 ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_machine:qemux86-64 ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_machine:qemumips64 ?= "4e9603752156d3a0b9fe53309f4bc78d3f4f7df5"
SRCREV_machine ?= "82689c62701573edb28ef1df2bd83adafd3ca701"
SRCREV_meta ?= "200c6fddf19c4a54c86dbfbe3d0f64597f5bc0c2"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "2b525314c7b57eac29fe8b77a6589428e4a4f6dd"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.19/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.19;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.19.17"

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

