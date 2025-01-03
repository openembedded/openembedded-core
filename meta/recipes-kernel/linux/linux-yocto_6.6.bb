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

SRCREV_machine:qemuarm ?= "ac47e2d574643c2f15824f571f00287536385207"
SRCREV_machine:qemuarm64 ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemuloongarch64 ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemumips ?= "88054d5c80cf5d95abb808479640564c45e416d1"
SRCREV_machine:qemuppc ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemuriscv64 ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemuriscv32 ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemux86 ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemux86-64 ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_machine:qemumips64 ?= "c3d3e3fa5f4c9de29e7c0cf3a85d2a959ad4f155"
SRCREV_machine ?= "77698884d242eb893d8c6de2d49318561ef0b39f"
SRCREV_meta ?= "82e71c6dcc562dbf7725e5ef994ef2ecd74e704c"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "a66cdcdc9e44b4c508190ea3cde5750954d1c4eb"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.6/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.6.66"

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

