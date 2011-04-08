SUMMARY = "Keyboard maps"
DESCRIPTION = "Keymaps and initscript to set the keymap on bootup."
SECTION = "base"
RDEPENDS_${PN} = "initscripts console-tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r18"

inherit update-rc.d

SRC_URI = "file://keymap.sh \
	   file://GPLv2.patch"

SRC_URI_append_c7x0         = " file://keymap-*.map"
SRC_URI_append_tosa         = " file://keymap-*.map"
SRC_URI_append_akita        = " file://keymap-*.map"
SRC_URI_append_spitz        = " file://keymap-*.map"
SRC_URI_append_collie       = " file://keymap-*.map"
SRC_URI_append_poodle       = " file://keymap-*.map"
SRC_URI_append_jornada6xx   = " file://keymap-*.map"
SRC_URI_append_h2200        = " file://keymap-*.map"
SRC_URI_append_htcuniversal = " file://keymap-*.map"

INITSCRIPT_NAME = "keymap.sh"
INITSCRIPT_PARAMS = "start 01 S ."

do_install () {
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/keymap.sh ${D}${sysconfdir}/init.d/

    case ${MACHINE} in
        c7x0 | tosa | spitz | akita | borzoi | collie | poodle | jornada6xx | h2200 | htcuniversal )
            install -m 0644 ${WORKDIR}/keymap-*.map	${D}${sysconfdir}
            ;;
        *)
            ;;
    esac
}
