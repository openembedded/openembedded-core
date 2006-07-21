DESCRIPTION = "Keyboard map"
SECTION = "base"
MAINTAINER = "Marcin Juszkiewicz <openembedded@hrw.one.pl>"
RDEPENDS = "initscripts console-tools"
LICENSE = "GPL"
PACKAGE_ARCH = "${MACHINE}"
PR = "r5"

inherit update-rc.d

SRC_URI = "file://keymap"

SRC_URI_append_c7x0         = " file://keymap-*.map"
SRC_URI_append_tosa         = " file://keymap-*.map"
SRC_URI_append_akita        = " file://keymap-*.map"
SRC_URI_append_spitz        = " file://keymap-*.map"
SRC_URI_append_collie       = " file://keymap-*.map"
SRC_URI_append_poodle       = " file://keymap-*.map"

INITSCRIPT_NAME = "keymap"
INITSCRIPT_PARAMS = "start 00 S ."

do_install () {
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/keymap ${D}${sysconfdir}/init.d/

    case ${MACHINE} in
        c7x0 | tosa | spitz | akita | collie | poodle )
            install -m 0644 ${WORKDIR}/keymap-*.map	${D}${sysconfdir}
            ;;
        *)
            ;;
    esac
}
