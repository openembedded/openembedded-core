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

SRCREV_machine_qemuarm ?= "4e354f7fa345464f284525a27eb1544cbc60968d"
SRCREV_machine_qemumips  ?= "c65bc98cd501459bb6ffc7b5ec5a7e72c41dceef"
SRCREV_machine_qemumipsel ?= "c65bc98cd501459bb6ffc7b5ec5a7e72c41dceef"
SRCREV_machine_qemuppc ?= "ee3475b4ca8c9e26f55f6e107101d832e7dec7ac"
SRCREV_machine_qemux86 ?= "a1cdb60720c452c3965eaec3ec2cd10f06261cc5"
SRCREV_machine_qemux86-64 ?= "a1cdb60720c452c3965eaec3ec2cd10f06261cc5"
SRCREV_machine ?= "a1cdb60720c452c3965eaec3ec2cd10f06261cc5"
SRCREV_meta ?= "51f94f06fe908a910564687942fdd82809f82348"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.3"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
