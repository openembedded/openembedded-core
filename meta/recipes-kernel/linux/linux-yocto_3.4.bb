require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemumipsel = "standard/mti-malta32"
KBRANCH_qemumips64 = "standard/mti-malta64"
KBRANCH_qemumips64el = "standard/mti-malta64"
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"

SRCREV_machine_qemuarm ?= "168ed084c978a39eabf0071f0049b623bc05c856"
SRCREV_machine_qemumips  ?= "66dbebd43d65eb070e0afa25f63f6b69ddd47c65"
SRCREV_machine_qemuppc ?= "bc74ca65c9cae025861455d7f0a259458cd4a829"
SRCREV_machine_qemux86 ?= "d345461f0d52d1a82553c2edd845a8bee908923f"
SRCREV_machine_qemux86-64 ?= "d345461f0d52d1a82553c2edd845a8bee908923f"
SRCREV_machine ?= "d345461f0d52d1a82553c2edd845a8bee908923f"
SRCREV_meta ?= "9b7c74bc8e205edb403744098b9d62690c25803e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.4"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
