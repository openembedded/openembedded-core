DESCRIPTION = "sysprof - System-wide Performance Profiler for Linux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "gtk+ libglade"

SRCREV = "38a6af1f0a45e528fd2842983da71e0f23c70d6a"
PR = r1
PV = "1.1.6+git${SRCPV}"

SRC_URI = "git://git.gnome.org/sysprof;protocol=git \
           file://define-NT_GNU_BUILD_ID.patch \
	   file://0001-Fix-PowerPC-checks-for-__NR_perf_counter_open.patch \
          "

SRC_URI_append_arm  = " file://rmb-arm.patch"
SRC_URI_append_mips = " file://rmb-mips.patch"
SRC_URI_append_powerpc = " file://ppc-macro-fix.patch"

SRC_URI[md5sum]    = "80902a7b3d6f7cb83eb6b47e87538747"
SRC_URI[sha256sum] = "1c6403278fa4f3b37a1fb9f0784e496dce1703fe84ac03b2650bf469133a0cb3"

S = "${WORKDIR}/git"

inherit autotools
