SUMMARY = "Graphical trace viewer for Ftrace"
LICENSE = "GPLv2"

require trace-cmd.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://kernel-shark.c;beginline=6;endline=8;md5=2c22c965a649ddd7973d7913c5634a5e"

DEPENDS = "gtk+"
RDEPENDS_${PN} = "trace-cmd"

SRC_URI_append = "file://kernelshark-fix-syntax-error-of-shell.patch"

EXTRA_OEMAKE = "'CC=${CC}' 'AR=${AR}' 'prefix=${prefix}' gui"

do_install() {
	oe_runmake CC="${CC}" AR="${AR}" prefix="${prefix}" DESTDIR="${D}" install_gui
	rm -rf ${D}${datadir}/trace-cmd
	rmdir ${D}${datadir}
}
