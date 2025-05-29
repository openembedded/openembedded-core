KBRANCH ?= "v6.6/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.6.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.6/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.6/standard/qemuarm64"
KBRANCH:qemumips ?= "v6.6/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.6/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.6/standard/base"
KBRANCH:qemuriscv32  ?= "v6.6/standard/base"
KBRANCH:qemux86  ?= "v6.6/standard/base"
KBRANCH:qemux86-64 ?= "v6.6/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.6/standard/base"
KBRANCH:qemumips64 ?= "v6.6/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "46314bb9440c533de81ad6fd1e4465a1a92dbaf1"
SRCREV_machine:qemuarm64 ?= "c2536612ab8cdfef938781c99eb8ef57466740fc"
SRCREV_machine:qemuloongarch64 ?= "26ad3d3b1796f301f5491a94487ec7af0e5c4a34"
SRCREV_machine:qemumips ?= "48a91048f92b14d77663c5faae98a2154c0c406b"
SRCREV_machine:qemuppc ?= "a92a2bd6411a5dcf62ca06c947eab278edd56151"
SRCREV_machine:qemuriscv64 ?= "26ad3d3b1796f301f5491a94487ec7af0e5c4a34"
SRCREV_machine:qemuriscv32 ?= "26ad3d3b1796f301f5491a94487ec7af0e5c4a34"
SRCREV_machine:qemux86 ?= "26ad3d3b1796f301f5491a94487ec7af0e5c4a34"
SRCREV_machine:qemux86-64 ?= "26ad3d3b1796f301f5491a94487ec7af0e5c4a34"
SRCREV_machine:qemumips64 ?= "3ef0bab27a99c6e29e97ef4e71a7967b31b4f562"
SRCREV_machine ?= "26ad3d3b1796f301f5491a94487ec7af0e5c4a34"
SRCREV_meta ?= "60d2dc333c5a027c81bf12f650deebcf4337a5b0"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "71e4ec9b2abccce5adecb3b354d0d5003f6f88f2"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.6/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.6.89"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "arm/versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32|qemuloongarch64)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc features/nf_tables/nft_test.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc features/gpio/sim.scc", "", d)}"
# libteam ptests from meta-oe needs it
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/net/team/team.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

