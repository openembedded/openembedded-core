DESCRIPTION = "sysprof - System-wide Performance Profiler for Linux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "gtk+ libglade"

SRCREV = "4692f85f625f4fd969cef4ab5dc47cc4655c87f9"
PR = "r2"
PV = "1.1.8+git${SRCPV}"

SRC_URI = "git://git.gnome.org/sysprof;protocol=git \
           file://define-NT_GNU_BUILD_ID.patch \
	   file://0001-Fix-PowerPC-checks-for-__NR_perf_counter_open.patch \
          "

SRC_URI_append_arm  = " file://rmb-arm.patch"
SRC_URI_append_mips = " file://rmb-mips.patch"

S = "${WORKDIR}/git"

inherit autotools
