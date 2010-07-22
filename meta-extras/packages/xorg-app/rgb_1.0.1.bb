require xorg-app-common.inc
PE = "1"

DEPENDS += " xproto util-macros"

FILES_${PN} += "${datadir}/X11"
