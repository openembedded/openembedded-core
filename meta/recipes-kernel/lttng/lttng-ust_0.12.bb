SUMMARY = "Linux Trace Toolkit Userspace Tracer"
DESCRIPTION = "The LTTng Userspace Tracer (UST) is a library accompanied by a set of tools to trace userspace code"
HOMEPAGE = "http://lttng.org/ust"
BUGTRACKER = "n/a"

LICENSE = "LGPLv2.1+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e647752e045a8c45b6f583771bd561ef \
                    file://ustctl/ustctl.c;endline=16;md5=b50c6fa0307175bb1ce0db49d752c03b \
                    file://snprintf/various.h;endline=31;md5=89f2509b6b4682c4fc95255eec4abe44"

DEPENDS = "liburcu"

PR = "r0"

SRC_URI = "http://lttng.org/files/ust/releases/ust-${PV}.tar.gz"

SRC_URI[md5sum] = "fae6e324a56016658c5b83ec14ba0043"
SRC_URI[sha256sum] = "215b05f47d0c9e3a2934926a7d14fa9c67025db917c3a1f641df40b08314ab0c"

S = "${WORKDIR}/ust-${PV}"

inherit autotools

# Due to liburcu not building on MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/arm/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'

