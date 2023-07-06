KBRANCH ?= "v6.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
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

SRCREV_machine:qemuarm ?= "fccbb53f81908991a69214e09ae0eea360410617"
SRCREV_machine:qemuarm64 ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemuloongarch64 ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemumips ?= "8370d3d4823bcff9532dd6bb163359f75a5416cd"
SRCREV_machine:qemuppc ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemuriscv64 ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemuriscv32 ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemux86 ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemux86-64 ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_machine:qemumips64 ?= "efe0a0701a1a8c5b6113d6a06b65857f8c84ac9f"
SRCREV_machine ?= "154c43de4e13946ccecf0ff13772db6c2adba355"
SRCREV_meta ?= "f15c15c0755e36ec185927dd83a8a569186836eb"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "0f4ac6b4c5f00f45b7a429c8a5b028a598c6400c"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.4/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.4;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.4"

PV = "${LINUX_VERSION}+git${SRCPV}"

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

