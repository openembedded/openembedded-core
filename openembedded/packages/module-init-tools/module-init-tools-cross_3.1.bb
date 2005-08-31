LICENSE = "GPL"
include module-init-tools_${PV}.bb
inherit cross
DEFAULT_PREFERENCE = "0"
PROVIDES += "virtual/${TARGET_PREFIX}depmod virtual/${TARGET_PREFIX}depmod-2.6"

PR="r3"

# When cross compiling depmod as shipped cannot handle endian
# differences between host and target, this fixes the problem.
# It also solves any possible issues with alignment (only likely
# if cross compiling for a low alignment target - e.g. x86, on
# a high alignment host - e.g. SPARC).
SRC_URI += " file://depmod-byteswap.patch;patch=1 "

EXTRA_OECONF_append = " --program-prefix=${TARGET_PREFIX}"

do_stage () {
        oe_runmake install
}

do_install () {
        :
}
