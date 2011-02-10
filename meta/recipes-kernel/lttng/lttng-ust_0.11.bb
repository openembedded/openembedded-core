SUMMARY = "Linux Trace Toolkit Userspace Tracer"
DESCRIPTION = "The LTTng Userspace Tracer (UST) is a library accompanied by a set of tools to trace userspace code"
HOMEPAGE = "http://lttng.org/ust"
BUGTRACKER = "n/a"

LICENSE = "LGPLv2.1+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e647752e045a8c45b6f583771bd561ef \
                    file://ustctl/ustctl.c;endline=16;md5=eceeaab8a5574f24d62f7950b9d1adf4 \
                    file://snprintf/various.h;endline=31;md5=89f2509b6b4682c4fc95255eec4abe44"

DEPENDS = "liburcu"

PR = "r1"

SRC_URI = "http://lttng.org/files/ust/releases/ust-${PV}.tar.gz \
           file://remove_ppc_specific_time_reading_function.patch \
           "

SRC_URI[md5sum] = "0a23fa60df4da3fb5188e314001eb49c"
SRC_URI[sha256sum] = "af8f699019ae260103bb401b6738d5e417e79732a509859b42a52e9a0f5edb35"

S = "${WORKDIR}/ust-${PV}"

inherit autotools

# Due to liburcu not building on ARM or MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|powerpc.*)-linux'



