SUMMARY = "Babeltrace - Trace Format Babel Tower"
DESCRIPTION = "Babeltrace provides trace read and write libraries in host side, as well as a trace converter, which used to convert LTTng 2.0 traces into human-readable log."
HOMEPAGE = "http://www.efficios.com/babeltrace/"
BUGTRACKER = "n/a"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76ba15dd76a248e1dd526bca0e2125fa"

inherit autotools

DEPENDS = "glib-2.0 util-linux popt"

SRCREV = "9eaf25433864cefc40242e0257a0177ef4515930"
PV = "1.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.efficios.com/babeltrace.git;protocol=git"

S = "${WORKDIR}/git"

do_configure_prepend () {
	${S}/bootstrap
}

# Due to liburcu not building for MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/arm/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux.*'
