SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://www.efficios.com/babeltrace/"
BUGTRACKER = "n/a"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8d1a03b3c17bdd158b3cbb34813b1423"

inherit autotools

DEPENDS = "gtk+ util-linux"

SRCREV = "efc5077568403009493d1d72e94f292838825e51"
PV = "0.8+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.efficios.com/babeltrace.git;protocol=git"

S = "${WORKDIR}/git"

do_configure_prepend () {
	${S}/bootstrap
}

# Due to liburcu not building for MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/arm/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'
