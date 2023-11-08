KBRANCH ?= "v6.5/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.5.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.5/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.5/standard/qemuarm64"
KBRANCH:qemumips ?= "v6.5/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.5/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.5/standard/base"
KBRANCH:qemuriscv32  ?= "v6.5/standard/base"
KBRANCH:qemux86  ?= "v6.5/standard/base"
KBRANCH:qemux86-64 ?= "v6.5/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.5/standard/base"
KBRANCH:qemumips64 ?= "v6.5/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "38c4fee81a4d2da3be10690f5b78151e0bd544ac"
SRCREV_machine:qemuarm64 ?= "7358f19647fcab467f6d00d9a24aec8e99f8cd00"
SRCREV_machine:qemuloongarch64 ?= "8503729f1f61e7464270af24a6a9e10460158d4e"
SRCREV_machine:qemumips ?= "5b03adb9d873c62991c87fed8a186bbd8d65987c"
SRCREV_machine:qemuppc ?= "7c2b15eb10d4337712627cd70d668184bfd0d177"
SRCREV_machine:qemuriscv64 ?= "8503729f1f61e7464270af24a6a9e10460158d4e"
SRCREV_machine:qemuriscv32 ?= "8503729f1f61e7464270af24a6a9e10460158d4e"
SRCREV_machine:qemux86 ?= "8503729f1f61e7464270af24a6a9e10460158d4e"
SRCREV_machine:qemux86-64 ?= "8503729f1f61e7464270af24a6a9e10460158d4e"
SRCREV_machine:qemumips64 ?= "ce7978769497891f4d4a7c0b310515febd208b6d"
SRCREV_machine ?= "8503729f1f61e7464270af24a6a9e10460158d4e"
SRCREV_meta ?= "1920717e6c53d0bdd0ebb6aa943657854c44b5db"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "8bbe7c640d76724e9cfd8aa130b8d36ad6db77a9"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.5/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.5;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.5.8"

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
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

