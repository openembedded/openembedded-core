require shared-mime-info.inc

PR = "r1"

inherit native

DEPENDS = "libxml2-native intltool-native glib-2.0-native"

S = "${WORKDIR}/shared-mime-info-${PV}"
