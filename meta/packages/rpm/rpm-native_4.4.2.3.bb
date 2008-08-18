require rpm_${PV}.bb
inherit native

DEPENDS = "beecrypt-native zlib-native file-native popt-native"

PR = "r2"

export sharedstatedir = "${layout_sharedstatedir}"
export localstatedir = "${layout_localstatedir}"

