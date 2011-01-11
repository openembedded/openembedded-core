DESCRIPTION = "User interface to Ftrace"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://trace-cmd.c;beginline=6;endline=8;md5=2c22c965a649ddd7973d7913c5634a5e"

PR = r0
PV = "1.0.5+git${SRCPV}"

inherit pkgconfig

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git;protocol=git"
S = "${WORKDIR}/git"

EXTRA_OEMAKE = "'prefix=${prefix}'"

do_install() {
	oe_runmake prefix="${prefix}" DESTDIR="${D}" install
}
