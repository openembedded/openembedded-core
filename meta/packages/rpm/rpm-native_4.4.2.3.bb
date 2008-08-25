require rpm_${PV}.bb
inherit native

DEPENDS = "beecrypt-native zlib-native file-native popt-native python-native"

PR = "r2"

export sharedstatedir = "${layout_sharedstatedir}"
export localstatedir = "${layout_localstatedir}"
