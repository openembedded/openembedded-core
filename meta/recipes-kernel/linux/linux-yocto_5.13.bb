KBRANCH ?= "v5.13/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.13/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.13/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.13/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.13/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.13/standard/base"
KBRANCH:qemuriscv32  ?= "v5.13/standard/base"
KBRANCH:qemux86  ?= "v5.13/standard/base"
KBRANCH:qemux86-64 ?= "v5.13/standard/base"
KBRANCH:qemumips64 ?= "v5.13/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "e85bc7dd9373a476d7a190b31aed1b95811a82ee"
SRCREV_machine:qemuarm64 ?= "a88c6ef395d5fb8e0b6d8df3e1099b7e5b584968"
SRCREV_machine:qemumips ?= "c040b29d5eda76464d67b59373977f289d3646df"
SRCREV_machine:qemuppc ?= "5c6982e564b80ec80f77d728186f7b29ff62898f"
SRCREV_machine:qemuriscv64 ?= "609a8a548c0931b07afb0f8b92068ec10e306aa5"
SRCREV_machine:qemuriscv32 ?= "609a8a548c0931b07afb0f8b92068ec10e306aa5"
SRCREV_machine:qemux86 ?= "609a8a548c0931b07afb0f8b92068ec10e306aa5"
SRCREV_machine:qemux86-64 ?= "609a8a548c0931b07afb0f8b92068ec10e306aa5"
SRCREV_machine:qemumips64 ?= "583c978f97f6e1b779f2210e74907d0c1c3da9a2"
SRCREV_machine ?= "609a8a548c0931b07afb0f8b92068ec10e306aa5"
SRCREV_meta ?= "2b89265a0bf39358c674a8e0f38552f6adec6ad8"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE:class-devupstream = "-1"
SRCREV_machine:class-devupstream ?= "f259ee2f037925eaf3d0c53f7d7aa2d3fae4ea13"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.13/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13.9"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

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
