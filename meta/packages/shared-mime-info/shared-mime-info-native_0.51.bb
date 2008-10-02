require shared-mime-info.inc

DEPENDS = "libxml2-native intltool-native glib-2.0-native"

inherit native

S = "${WORKDIR}/shared-mime-info-${PV}"
