SUMMARY = "User interface to Ftrace"
LICENSE = "GPLv2 & LGPLv2.1"

require trace-cmd.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://trace-cmd.c;beginline=6;endline=8;md5=2c22c965a649ddd7973d7913c5634a5e \
                    file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff \
                    file://trace-input.c;beginline=5;endine=8;md5=6ad47cc2b03385d8456771eec5eeea0b"

SRC_URI_append = "file://trace-cmd-Add-checks-for-invalid-pointers-to-fix-seg.patch \
           file://trace-cmd-Do-not-call-stop_threads-if-doing-latency-.patch \
           file://trace-cmd-Setting-plugin-to-nop-clears-data-before-i.patch \
           file://trace-cmd-fix-syntax-error-of-shell.patch \
"

EXTRA_OEMAKE = "'prefix=${prefix}'"

FILES_${PN}-dbg += "${datadir}/trace-cmd/plugins/.debug/"

do_install() {
	oe_runmake prefix="${prefix}" DESTDIR="${D}" install
}
