DESCRIPTION = "User interface to Ftrace"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

PR = r1
PV = "1.0.4+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git;protocol=git"
S = "${WORKDIR}/git"


do_compile() { 
	oe_runmake ARCH="${ARCH}" CC="${CC}" LD="${LD}" prefix=${prefix}
}

do_install() {
	oe_runmake ARCH="${ARCH}" CC="${CC}" LD="${LD}" \
                   prefix=${prefix} DESTDIR=${D} install
}
