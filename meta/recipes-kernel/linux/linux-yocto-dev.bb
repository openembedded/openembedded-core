# This recipe tracks the linux-yocto-dev repository as its upstream source.
# Since this tree is frequently updated, and periodically rebuilt, AUTOREV is
# used to track its contents.
#
# This recipe is just like other linux-yocto variants, with the only difference
# being that to avoid network access during initial parsing, static SRCREVs are
# provided and overridden if the preferred kernel provider is linux-yocto-dev.
#
# To enable this recipe, set PREFERRED_PROVIDER_virtual/kernel = "linux-yocto-dev"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"
KBRANCH_DEFAULT = "${KBRANCH}"
KMETA = "meta"

SRC_URI = "git://git.pokylinux.org/linux-yocto-dev.git;protocol=git;nocheckout=1;branch=${KBRANCH},${KMETA};name=machine,meta"

# Set default SRCREVs. Both the machine and meta SRCREVs are statically set
# to the korg v3.7 tag, and hence prevent network access during parsing. If
# linux-yocto-dev is the preferred provider, they will be overridden to
# AUTOREV in following anonymous python routine and resolved when the
# variables are finalized.
SRCREV_machine="29594404d7fe73cd80eaa4ee8c43dcc53970c60e"
SRCREV_meta="29594404d7fe73cd80eaa4ee8c43dcc53970c60e"

python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel", True) != "linux-yocto-dev":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-yocto-dev to enable it")
    else:
        d.setVar("SRCREV_machine", "${AUTOREV}")
        d.setVar("SRCREV_meta", "${AUTOREV}")
}

LINUX_VERSION ?= "3.8+"
LINUX_VERSION_EXTENSION ?= "-yoctodev-${LINUX_KERNEL_TYPE}"
PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
