# To enable LTO, add following in local.conf
# require conf/distro/include/lto.inc
# DISTRO_FEATURES_append = " lto"
#

# Disable LTO for following packages
LTO_pn-glibc = ""
LTO_pn-gcc-runtime = ""
LTO_pn-libgcc-initial = ""
LTO_pn-libgcc = ""
LTO_pn-libpam = ""
LTO_pn-elfutils = ""
LTO_pn-perl = ""
LTO_pn-busybox = ""
LTO_pn-libxcrypt = ""
LTO_pn-curl = ""
LTO_pn-libcap = ""
LTO_pn-libproxy = ""
LTO_pn-libbsd = ""

# Override it for additional or different options if needed e.g.
# with clang thin-lto might be better for compile speed
LTO ?= "-flto"

SELECTED_OPTIMIZATION_append = "${@bb.utils.contains('DISTRO_FEATURES', 'lto', ' ${LTO}', '', d)}"
TARGET_LDFLAGS_append_class-target = "${@bb.utils.contains('DISTRO_FEATURES', 'lto', ' ${LTO}', '', d)}"

SELECTED_OPTIMIZATION[vardeps] += "LTO"
