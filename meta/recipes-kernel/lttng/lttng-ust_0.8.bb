SUMMARY = "Linux Trace Toolkit Userspace Tracer"
DESCRIPTION = "The LTTng Userspace Tracer (UST) is a library accompanied by a set of tools to trace userspace code"
HOMEPAGE = "http://lttng.org/ust"
BUGTRACKER = "n/a"

# In the source directory, doc/manual/ust.html says ustctl/libustcmd/ustd are
# licensed as GPL v2, but the source codes of ustctl/libustcmd/ustd have a
# LGPLv2.1+ header; the files in snprintf/ have a BSD header.
LICENSE = "LGPV2.1+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e647752e045a8c45b6f583771bd561ef \
                    file://ustctl/ustctl.c;endline=16;md5=eceeaab8a5574f24d62f7950b9d1adf4 \
                    file://snprintf/various.h;endline=31;md5=89f2509b6b4682c4fc95255eec4abe44"

DEPENDS = "liburcu"

PR = "r0"

SRC_URI = "http://lttng.org/files/ust/releases/ust-${PV}.tar.gz"

SRC_URI[md5sum] = "333ff7c8fef92b31dff85789865bb791"
SRC_URI[sha256sum] = "802398bc2c3ad5c80bd236d1ee24968f4da8a1401695e8dc15cb396ac836abb4"

S = "${WORKDIR}/ust-${PV}"

inherit autotools

# Due to liburcu not building on ARM or MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|powerpc.*)-linux'



