require linux-moblin.inc

PR = "r0"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_menlow = "100"

SRC_URI = "git://git.moblin.org/projects/kernel-mid-2.6.24.git;protocol=git \
           file://defconfig-menlow"

S = "${WORKDIR}/git"
