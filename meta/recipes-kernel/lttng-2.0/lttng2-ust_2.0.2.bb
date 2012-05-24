SUMMARY = "Linux Trace Toolkit Userspace Tracer 2.0"
DESCRIPTION = "The LTTng UST 2.0 package contains the userspace tracer library to trace userspace codes."
HOMEPAGE = "http://lttng.org/lttng2.0"
BUGTRACKER = "n/a"

LICENSE = "LGPLv2.1+ & BSD & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=c963eb366b781252b0bf0fdf1624d9e9 \
                    file://snprintf/snprintf.c;endline=32;md5=d3d544959d8a3782b2e07451be0a903c \
                    file://snprintf/various.h;endline=31;md5=89f2509b6b4682c4fc95255eec4abe44"

inherit autotools

DEPENDS = "liburcu util-linux"

SRCREV = "a367ee66aad3ffd21ef64d1b24efc6f862e09562"
PV = "2.0.2+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.lttng.org/lttng-ust.git;protocol=git"

S = "${WORKDIR}/git"

do_configure_prepend () {
	${S}/bootstrap
}

# Due to liburcu not building for MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/arm/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'
