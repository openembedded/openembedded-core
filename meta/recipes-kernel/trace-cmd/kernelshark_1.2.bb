DESCRIPTION = "Graphical trace viewer for Ftrace"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://kernel-shark.c;beginline=6;endline=8;md5=2c22c965a649ddd7973d7913c5634a5e"

SRCREV = "7055ffd37beeb44714e86a4abc703f7e175a0db5"
PR = "r1"
PV = "1.2+git${SRCPV}"

DEPENDS = "gtk+"
RDEPENDS_${PN} = "trace-cmd"

inherit pkgconfig

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git;protocol=git \
           file://addldflags.patch \
           file://make-docs-optional.patch \
           file://blktrace-api-compatibility.patch"
S = "${WORKDIR}/git"

EXTRA_OEMAKE = "'CC=${CC}' 'AR=${AR}' 'prefix=${prefix}' gui"

FILES_${PN} += "${datadir}/trace-cmd/plugins/*.so"
FILES_${PN}-dbg += "${datadir}/trace-cmd/plugins/.debug/"

FILESPATH = "${FILE_DIRNAME}/trace-cmd"

do_install() {
	oe_runmake CC="${CC}" AR="${AR}" prefix="${prefix}" DESTDIR="${D}" install_gui
}
