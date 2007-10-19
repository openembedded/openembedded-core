DESCRIPTION = "Keyboard map"
SECTION = "base"
RDEPENDS = "initscripts console-tools"
LICENSE = "GPL"
PACKAGE_ARCH = "${MACHINE}"
PR = "r15"

inherit update-rc.d

SRC_URI = "file://keymap"

SRC_URI_append_c7x0         = " file://keymap-*.map"
SRC_URI_append_tosa         = " file://keymap-*.map"
SRC_URI_append_akita        = " file://keymap-*.map"
SRC_URI_append_spitz        = " file://keymap-*.map"
SRC_URI_append_collie       = " file://keymap-*.map"
SRC_URI_append_poodle       = " file://keymap-*.map"
SRC_URI_append_jornada6xx   = " file://keymap-*.map"
SRC_URI_append_h2200        = " file://keymap-*.map"
SRC_URI_append_htcuniversal = " file://keymap-*.map"

INITSCRIPT_NAME = "keymap"
INITSCRIPT_PARAMS = "start 01 S ."

do_install () {
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/keymap ${D}${sysconfdir}/init.d/

    case ${MACHINE} in
        c7x0 | tosa | spitz | akita | borzoi | collie | poodle | jornada6xx | h2200 | htcuniversal )
            install -m 0644 ${WORKDIR}/keymap-*.map	${D}${sysconfdir}
            ;;
        *)
            ;;
    esac
}
