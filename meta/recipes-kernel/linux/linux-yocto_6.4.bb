KBRANCH ?= "v6.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.4.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.4/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.4/standard/qemuarm64"
KBRANCH:qemumips ?= "v6.4/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.4/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.4/standard/base"
KBRANCH:qemuriscv32  ?= "v6.4/standard/base"
KBRANCH:qemux86  ?= "v6.4/standard/base"
KBRANCH:qemux86-64 ?= "v6.4/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.4/standard/base"
KBRANCH:qemumips64 ?= "v6.4/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "5f3231da12226c3a1f30a6074691adeb50e87e29"
SRCREV_machine:qemuarm64 ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemuloongarch64 ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemumips ?= "cd6f4d29a436519a02fb03788ffcd1b537311e74"
SRCREV_machine:qemuppc ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemuriscv64 ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemuriscv32 ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemux86 ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemux86-64 ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_machine:qemumips64 ?= "c66efea0d1d1d36395ee98abe1d912b6b4b42885"
SRCREV_machine ?= "8d7f22bfccbb65c1c13ec9e295b5af5a37755213"
SRCREV_meta ?= "e470f033e5577e2e9bb256ce3b419541c4829364"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "05d8970cca014b96c06c3730ae084f08087f13dd"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.4/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.4;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.4.12"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

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

